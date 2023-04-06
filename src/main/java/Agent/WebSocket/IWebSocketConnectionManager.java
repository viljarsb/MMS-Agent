package Agent.WebSocket;

import Agent.Agent.AnonymousAdapter;
import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.IAnonymousConnection;
import Agent.Connections.IAuthenticatedConnection;
import Agent.Connections.IConnection;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IWebSocketConnectionManager
{
    CompletableFuture<IAnonymousConnection> connectAnonymous(RouterInfo routerInfo, TLSConfiguration tlsConfig, AnonymousAdapter listener);
    CompletableFuture<IAuthenticatedConnection> connectAuthenticated(RouterInfo routerInfo, mTLSConfiguration tlsConfig, AuthenticatedAdapter listener);
    void close();
    void close(int statusCode, String reason);
    List<IConnection> getConnections();
}
