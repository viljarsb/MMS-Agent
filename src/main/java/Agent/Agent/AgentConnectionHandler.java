package Agent.Agent;

import Agent.Connections.IConnection;
import Agent.ServiceDiscovery.mDNSRouterDiscoveryService;
import Agent.Utils.ExecutorFactory;
import Agent.Connections.IAnonymousConnection;
import Agent.Connections.IAuthenticatedConnection;
import Agent.Exceptions.AgentConnectionHandlerInitException;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.ImDNSRouterDiscoveryService;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;
import Agent.WebSocket.IWebSocketConnectionManager;
import Agent.WebSocket.WebSocketConnectionManager;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Implementation of the {@link IAgentConnectionHandler} interface.
 * This class is responsible for discovering routers on the local network and allowing the user to connect to them.
 * A instance of this class can initiate multiple connections to routers.
 */
public class AgentConnectionHandler implements IAgentConnectionHandler
{
    private final ImDNSRouterDiscoveryService discoveryService;
    private final IWebSocketConnectionManager connectionManager;


    /**
     * Constructor for the AgentConnectionHandler class that takes an InetAddress object representing the address to listen on for router discovery and WebSocket connections.
     * Initializes the discovery service and connection manager using the provided executor.
     *
     * @param address An InetAddress object representing the address to listen on for router discovery and WebSocket connections.
     * @throws AgentConnectionHandlerInitException if there is an error initializing the AgentConnectionHandler.
     */
    AgentConnectionHandler(InetAddress address) throws AgentConnectionHandlerInitException
    {
        Executor executor = ExecutorFactory.createWorkerPool();

        try
        {
            discoveryService = new mDNSRouterDiscoveryService(address, executor);
        }
        catch (IOException ex)
        {
            throw new AgentConnectionHandlerInitException("Failed to init agent connection handler", ex);
        }

        connectionManager = new WebSocketConnectionManager(address, executor);
    }


    /**
     * Discover routers on the local network, and return a CompletableFuture that completes with a list of RouterInfo objects.
     *
     * @return A CompletableFuture that completes with a list of RouterInfo objects.
     */
    @Override
    public CompletableFuture<List<RouterInfo>> discover()
    {
        return discoveryService.listen();
    }


    /**
     * Discover routers on the local network, and call the listener when the routers are discovered or not.
     *
     * @param listener The listener to call when a router is discovered.
     */
    @Override
    public void discover(IDiscoveryListener listener)
    {
        discoveryService.listen(listener);
    }


    /**
     * Connect anonymously to the specified router.
     *
     * @param routerInfo The router to connect to.
     * @param tlsConfig  The TLS configuration to use.
     * @param listener   The listener to call when the connection is established and when messages are received.
     * @return A CompletableFuture that completes with an IAnonymousConnection object. (the listener is also called when the connection is established)
     */
    @Override
    public CompletableFuture<IAnonymousConnection> connectAnonymously(RouterInfo routerInfo, TLSConfiguration tlsConfig, AnonymousAdapter listener)
    {
        return connectionManager.connectAnonymous(routerInfo, tlsConfig, listener);
    }


    /**
     * Connect authenticated to the specified router.
     *
     * @param routerInfo The router to connect to.
     * @param tlsConfig  The TLS configuration to use.
     * @param listener   The listener to call when the connection is established and when messages are received.
     * @return A CompletableFuture that completes with an IAuthenticatedConnection object. (the listener is also called when the connection is established)
     */
    @Override
    public CompletableFuture<IAuthenticatedConnection> connectAuthenticated(RouterInfo routerInfo, mTLSConfiguration tlsConfig, AuthenticatedAdapter listener)
    {
        return connectionManager.connectAuthenticated(routerInfo, tlsConfig, listener);
    }


    /**
     * Close the connection handler.
     * This will close all connections and stop the discovery service.
     */
    @Override
    public void close()
    {
        discoveryService.close();
        connectionManager.close();
    }


    /**
     * Get a list of all connections.
     *
     * @return A list of all connections.
     */
    @Override
    public List<IConnection> getConnections()
    {
        return connectionManager.getConnections();
    }
}
