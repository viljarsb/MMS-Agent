package SMMPClient.SMMPClient;

import Agent.Agent.AgentConnectionHandler;
import Agent.Connections.IAnonymousConnection;
import Agent.Connections.IAuthenticatedConnection;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;
import SMMPClient.Adapters.AnonymousAdapterImpl;
import SMMPClient.Adapters.AuthenticatedAdapterImpl;
import SMMPClient.Adapters.SMMPAnonAdapter;
import SMMPClient.Adapters.SMMPAuthAdapter;
import SMMPClient.Crypto.KeyringManager;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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


    public CompletableFuture<IAnonymousConnection> connectAnonymously(@NonNull RouterInfo routerInfo, @NonNull KeyringManager keyringManager, @NonNull SMMPAnonAdapter listener)
    {
        AnonymousAdapterImpl anonAdapter = new AnonymousAdapterImpl(keyringManager, listener);
        TLSConfiguration tlsConfiguration = new TLSConfiguration(keyringManager.getTruststorePath(), keyringManager.getTruststorePassword());
        return agentConnectionHandler.connectAnonymously(routerInfo, tlsConfiguration, anonAdapter);
    }


    public CompletableFuture<IAuthenticatedConnection> connectAuthenticated(@NonNull RouterInfo routerInfo, @NonNull KeyringManager keyringManager, @NonNull SMMPAuthAdapter listener)
    {
        AuthenticatedAdapterImpl authAdapter = new AuthenticatedAdapterImpl(keyringManager, listener);
        mTLSConfiguration tlsConfig = new mTLSConfiguration(keyringManager.getTruststorePath(), keyringManager.getTruststorePassword(), keyringManager.getKeystorePath(), keyringManager.getKeystorePassword());
        return agentConnectionHandler.connectAuthenticated(routerInfo, tlsConfig, authAdapter);
    }
}
