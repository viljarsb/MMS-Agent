package SMMPClient.SMMPClient;

import Agent.Agent.AgentConnectionHandler;
import Agent.Connections.IAnonymousConnection;
import Agent.Connections.IAuthenticatedConnection;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;
import SMMPClient.Acks.AckTracker;
import SMMPClient.Adapters.AnonymousAdapterImpl;
import SMMPClient.Adapters.AuthenticatedAdapterImpl;
import SMMPClient.Adapters.SMMPAnonAdapter;
import SMMPClient.Adapters.SMMPAuthAdapter;
import SMMPClient.Crypto.KeyringManager;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * A simple wrapper around {@link AgentConnectionHandler}
 */
public class SMMPConnectionHandler
{
    private final AgentConnectionHandler agentConnectionHandler;


    public SMMPConnectionHandler(AgentConnectionHandler connectionHandler)
    {
        this.agentConnectionHandler = connectionHandler;
    }


    public CompletableFuture<List<RouterInfo>> discover()
    {
        return agentConnectionHandler.discover();
    }


    public void discover(@NonNull IDiscoveryListener listener)
    {
        agentConnectionHandler.discover(listener);
    }


    public void connectAnonymously(@NonNull RouterInfo routerInfo, @NonNull KeyringManager keyringManager, @NonNull SMMPAnonAdapter adapter)
    {
        AnonymousAdapterImpl internalAdapter = new AnonymousAdapterImpl(keyringManager, adapter);
        TLSConfiguration tlsConfiguration = new TLSConfiguration(keyringManager.getTruststorePath(), keyringManager.getTruststorePassword());
        agentConnectionHandler.connectAnonymously(routerInfo, tlsConfiguration, internalAdapter);
    }


    public void connectAuthenticated(@NonNull RouterInfo routerInfo, @NonNull KeyringManager keyringManager, @NonNull SMMPAuthAdapter adapter)
    {
        AuthenticatedAdapterImpl internalAdapter = new AuthenticatedAdapterImpl(keyringManager, adapter);
        mTLSConfiguration tlsConfiguration = new mTLSConfiguration(keyringManager.getTruststorePath(), keyringManager.getTruststorePassword(), keyringManager.getKeystorePath(), keyringManager.getKeystorePassword());
        agentConnectionHandler.connectAuthenticated(routerInfo, tlsConfiguration, internalAdapter);
    }
}
