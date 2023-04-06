package Agent.Agent;

import Agent.Exceptions.AgentConnectionHandlerInitException;
import Agent.Utils.NetworkUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for creating instances of the {@link IAgentConnectionHandler}. Maintains a map of existing connection handlers
 * to reuse existing instances when possible, as they can share thread-pools and service discovery instances (these are not cheap).
 */
public class AgentConnectionHandlerFactory
{
    private static final Map<InetAddress, IAgentConnectionHandler> instances = new ConcurrentHashMap<>();

    private AgentConnectionHandlerFactory() {}


    /**
     * Creates a new instance of the IAgentConnectionHandler with the default InetAddress.
     * Will try to resolve the default InetAddress using {@link NetworkUtils#getDefaultInetAddress()}.
     * But there are no guarantees that this will work on all devices.
     *
     * @return A new instance of the {@link IAgentConnectionHandler}
     * @throws AgentConnectionHandlerInitException if there is an error initializing the connection handler.
     */
    public static IAgentConnectionHandler create() throws AgentConnectionHandlerInitException
    {
        InetAddress address;
        try
        {
            address = NetworkUtils.getDefaultInetAddress();
        }
        catch (UnknownHostException ex)
        {
            throw new AgentConnectionHandlerInitException("Failed to init agent connection handler", ex);
        }

        return create(address);
    }


    /**
     * Creates a new instance of the {@link IAgentConnectionHandler}  with the provided address. (will connect to the router at this address and discover other routers on the same network).
     * Will try to resolve the provided address using {@link NetworkUtils#resolveInetAddress(String)}.
     *
     * @param address The address of the interface to use to discover routers and connect to them.
     * @return A new instance of the {@link IAgentConnectionHandler} .
     * @throws AgentConnectionHandlerInitException if there is an error initializing the connection handler.
     */
    public static IAgentConnectionHandler create(String address) throws AgentConnectionHandlerInitException
    {
        InetAddress inetAddress;
        try
        {
            inetAddress = NetworkUtils.resolveInetAddress(address);
        }
        catch (UnknownHostException ex)
        {
            throw new AgentConnectionHandlerInitException("Failed to resolve InetAddress for " + address, ex);
        }
        return create(inetAddress);
    }

    /**
     * Creates a new instance of the {@link IAgentConnectionHandler}  with the provided InetAddress.
     *
     * @param address An InetAddress object representing the address to listen on for router discovery and WebSocket connections.
     * @return A new instance of the {@link IAgentConnectionHandler} .
     * @throws AgentConnectionHandlerInitException if there is an error initializing the connection handler.
     */
    private static IAgentConnectionHandler create(InetAddress address) throws AgentConnectionHandlerInitException
    {
        IAgentConnectionHandler instance = instances.get(address);
        if (instance == null)
        {
            instance = new AgentConnectionHandler(address);
            instances.put(address, instance);
        }
        return instance;
    }
}
