package Agent.Agent;

import Agent.Connections.AuthenticatedConnection;
import Agent.MessageHandler.DirectMessageListener;
import Agent.WebSocket.IConnectionListener;

/**
 * Listener for authenticated agent connections, extending the AgentAdapter interface, the IConnectionListener interface, and the DirectMessageListener interface.
 */
public interface AuthenticatedAdapter extends AgentAdapter, IConnectionListener, DirectMessageListener
{
    /**
     * Override this method from the IConnectionListener interface to handle connection events for authenticated agent connections.
     *
     * @param connection The connection that was established.
     */
    void onConnect(AuthenticatedConnection connection);


    /**
     * A default implementation of the onConnect method from the IConnectionListener interface.
     * This just calls the onConnect method with the AuthenticatedConnection object instead of the IConnection object.
     * Really just here to make the code a bit cleaner, so people who use this does not have to cast the IConnection object to an AuthenticatedConnection object.
     *
     * @param connection The connection that was established.
     */
    default void onConnect(Agent.Connections.IConnection connection)
    {
        onConnect((AuthenticatedConnection) connection);
    }
}
