package Agent.Agent;

import Agent.Connections.IAnonymousConnection;
import Agent.Connections.IAuthenticatedConnection;
import Agent.Connections.IConnection;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * Interface for the agent connection handler, specifying the methods that the users cal use to discover and connect to routers.
 * The agent also responsible for managing the connections it has established.
 */
public interface IAgentConnectionHandler
{
    /**
     * Discover routers on the local network.
     *
     * @return A CompletableFuture that completes with a list of RouterInfo objects.
     */
    CompletableFuture<List<RouterInfo>> discover();


    /**
     * Discover routers on the local network,
     * and call the listener when the routers is discovered or not.
     *
     * @param listener The listener to call when a router is discovered.
     */
    void discover(IDiscoveryListener listener);


    /**
     * Connect anonymously to the specified router.
     *
     * @param routerInfo The router to connect to.
     * @param tlsConfig  The TLS configuration to use.
     * @param listener   The listener to call when the connection is established and when messages are received.
     * @return A CompletableFuture that completes with an IAnonymousConnection object.
     */
    CompletableFuture<IAnonymousConnection> connectAnonymously(RouterInfo routerInfo, TLSConfiguration tlsConfig, AnonymousAdapter listener);


    /**
     * Connect authenticated to the specified router.
     *
     * @param routerInfo The router to connect to.
     * @param tlsConfig  The TLS configuration to use.
     * @param listener   The listener to call when the connection is established and when messages are received.
     * @return A CompletableFuture that completes with an IAuthenticatedConnection object.
     */
    CompletableFuture<IAuthenticatedConnection> connectAuthenticated(RouterInfo routerInfo, mTLSConfiguration tlsConfig, AuthenticatedAdapter listener);


    /**
     * Close all connections created by this instance and close the service discovery.
     */
    void close();


    /**
     * Get a list of all connections created by this instance.
     *
     * @return A list of all connections created by this instance.
     */
    List<IConnection> getConnections();
}
