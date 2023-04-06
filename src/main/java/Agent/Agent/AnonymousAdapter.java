package Agent.Agent;

import Agent.Connections.AnonymousConnection;
import Agent.MessageHandler.SubjectMessageListener;
import Agent.WebSocket.IConnectionListener;

/**
 * Listener for anonymous agent connections, extending the AgentAdapter interface, the IConnectionListener interface, and the SubjectMessageListener interface.
 */
public interface AnonymousAdapter extends AgentAdapter, IConnectionListener, SubjectMessageListener
{
    /**
     * Override this method from the IConnectionListener interface to handle connection events for anonymous agent connections.
     *
     * @param connection The connection that was established.
     */
    void onConnect(AnonymousConnection connection);


    default void onConnect(Agent.Connections.IConnection connection)
    {
        onConnect((AnonymousConnection) connection);
    }
}
