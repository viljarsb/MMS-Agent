package Agent.Connections;

import Agent.Connections.ConnectionStatus;

/**
 * An interface representing a connection to the edge router.
 */
public interface IConnection
{

    /**
     * Returns the IP address of the remote endpoint of the connection.
     *
     * @return The IP address of the remote endpoint of the connection.
     */
    String getConnectionAddress();

    /**
     * Returns whether the connection is still alive.
     *
     * @return True if the connection is still alive, false otherwise.
     */
    boolean isAlive();

    /**
     * Disconnects the connection with the specified status code and reason.
     *
     * @param statusCode The status code to send in the close frame.
     * @param reason     The reason for disconnecting.
     */
    void disconnect(int statusCode, String reason);

    /**
     * Disconnects the connection with no status code and no reason (Jetty will use something default).
     */
    void disconnect();

    ConnectionStatus getConnectionStatus();

    /**
     * Returns whether the connection is authenticated.
     *
     * @return True if the connection is authenticated, false otherwise.
     */
    boolean isAuthenticated();

    /**
     * Returns the unique ID of the connection.
     *
     * @return The unique ID of the connection.
     */
    String getConnectionID();
}
