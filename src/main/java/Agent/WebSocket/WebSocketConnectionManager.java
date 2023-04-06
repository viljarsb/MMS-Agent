package Agent.WebSocket;


import Agent.Agent.AgentAdapter;
import Agent.Agent.AnonymousAdapter;
import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AnonymousConnection;
import Agent.Connections.AuthenticatedConnection;
import Agent.Connections.IAnonymousConnection;
import Agent.Connections.IAuthenticatedConnection;
import Agent.Connections.IConnection;
import Agent.Exceptions.MMSSecurityException;
import Agent.MessageSending.MMTPMessageSender;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.Subscriptions.DmSubscriptionHandler;
import Agent.Subscriptions.SubjectSubscriptionHandler;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.maritimeconnectivity.pki.CertificateHandler;
import net.maritimeconnectivity.pki.PKIIdentity;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@Slf4j
public class WebSocketConnectionManager implements IWebSocketConnectionManager, DisconnectionHook
{
    private final Map<Session, IConnection> connectionMap = new ConcurrentHashMap<>();
    private final Executor executor;
    private final InetSocketAddress address;


    public WebSocketConnectionManager(@NonNull InetAddress address, @NonNull Executor executor)
    {
        this.address = new InetSocketAddress(address, 0);
        this.executor = executor;
    }


    @Override
    public CompletableFuture<IAnonymousConnection> connectAnonymous(
            @NonNull RouterInfo routerInfo,
            @NonNull TLSConfiguration tlsConfig,
            @NonNull AnonymousAdapter listener)
    {
        return connectInternal(routerInfo, tlsConfig.getTLSContextFactory(), listener)
                .thenApply(this::createAnonymousConnection);
    }


    private AnonymousConnection createAnonymousConnection(Session session)
    {
        AnonymousConnection connection = new AnonymousConnection(session, this, new SubjectSubscriptionHandler(session, executor));
        connectionMap.put(session, connection);
        return connection;
    }


    @Override
    public CompletableFuture<IAuthenticatedConnection> connectAuthenticated(
            @NonNull RouterInfo routerInfo,
            @NonNull mTLSConfiguration tlsConfig,
            @NonNull AuthenticatedAdapter listener)
    {
        try
        {
            PKIIdentity identity = extractIdentityFromTLSConfig(tlsConfig);
            return connectInternal(routerInfo, tlsConfig.getTLSContextFactory(), listener)
                    .thenApply(session -> createAuthenticatedConnection(session, identity));
        }

        catch (MMSSecurityException ex)
        {
            listener.onConnectionError(ex);
            return CompletableFuture.failedFuture(ex);
        }
    }


    private PKIIdentity extractIdentityFromTLSConfig(mTLSConfiguration tlsConfig) throws MMSSecurityException
    {
        try
        {
            SslContextFactory tlsContextFactory = tlsConfig.getTLSContextFactory();
            tlsContextFactory.getCertAlias();
            System.out.println("tlsContextFactory.getCertAlias() = " + tlsContextFactory.getCertAlias());

            KeyStore keyStore = tlsConfig.getTLSContextFactory().getKeyStore();

            Enumeration<String> aliases = keyStore.aliases();

            String alias = null;
            while (aliases.hasMoreElements())
            {
                String currentAlias = aliases.nextElement();
                if (keyStore.isCertificateEntry(currentAlias))
                {
                    alias = currentAlias;
                    break;
                }
            }

            if (alias == null)
            {
                throw new MMSSecurityException("No certificate found in KeyStore.");
            }

            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

            return CertificateHandler.getIdentityFromCert(certificate);
        }
        catch (Exception ex)
        {
            throw new MMSSecurityException("Error occurred while retrieving certificate from keystore.", ex);
        }
    }


    private AuthenticatedConnection createAuthenticatedConnection(Session session, PKIIdentity identity)
    {
        AuthenticatedConnection connection = new AuthenticatedConnection(session, this, new DmSubscriptionHandler(session, executor),
                new MMTPMessageSender(session, identity.getMrn(), executor), identity);

        connectionMap.put(session, connection);
        return connection;
    }


    private CompletableFuture<Session> connectInternal(@NonNull RouterInfo routerInfo, @NonNull SslContextFactory tlsContextFactory, @NonNull AgentAdapter listener)
    {
        IConnectionListener connectionListener = (IConnectionListener) listener;

        return CompletableFuture.supplyAsync(() ->
        {
            try
            {
                URI uri = routerInfo.getURI();
                return connect(uri, tlsContextFactory, listener);
            }
            catch (Exception ex)
            {
                log.error("Error occurred while connecting to router.", ex);
                connectionListener.onConnectionError(ex);
                throw new CompletionException(ex);
            }
        }, executor).orTimeout(5, TimeUnit.SECONDS).exceptionally(throwable ->
        {
            log.error("Error occurred while connecting to router.", throwable);
            connectionListener.onConnectionError(throwable);
            return null;
        });
    }


    private Session connect(@NonNull URI uri, @NonNull SslContextFactory tlsContextFactory, @NonNull AgentAdapter listener) throws Exception
    {
        HttpClient httpClient = new HttpClient(tlsContextFactory);
        WebSocketClient client = new WebSocketClient(httpClient);
        // httpClient.setBindAddress(address);
        IConnection connection = null;
        try
        {
            httpClient.start();
            client.start();

            ClientUpgradeRequest request = new ClientUpgradeRequest();
            request.setHeader("Sec-WebSocket-Protocol", "MMTP/1.0");

            WebSocketHandler wsAdapter = new WebSocketHandler(listener, this, executor);
            Future<Session> future = client.connect(wsAdapter, uri, request);
            return future.get(5, TimeUnit.SECONDS);
        }

        catch (Exception ex)
        {
            log.error("Error occurred while connecting to router.", ex);
            ((IConnectionListener) listener).onConnectionError(ex);
        }

        return null;
    }


    @Override
    public void onDisconnect(Session session)
    {
        connectionMap.remove(session);
    }


    @Override
    public void close()
    {
        for (IConnection connection : connectionMap.values())
        {
            connection.disconnect();
        }
    }


    @Override
    public void close(int statusCode, String reason)
    {
        for (IConnection connection : connectionMap.values())
        {
            connection.disconnect(statusCode, reason);
        }
    }


    @Override
    public List<IConnection> getConnections()
    {
        return new ArrayList<>(connectionMap.values());
    }
}

