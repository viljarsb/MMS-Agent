package Agent.Connections;


import Agent.Exceptions.ConnectException;
import Agent.WebSocket.DisconnectionHook;
import org.eclipse.jetty.websocket.api.Session;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * An abstract class representing a connection to the edge router.
 * An implementation of the {@link IConnection} interface.
 */
public abstract class Connection implements IConnection
{
    protected String connectionID;
    protected Session session;
    protected DisconnectionHook disconnectionHook;
    protected ConnectionStatus connectionStatus;


    /**
     * Creates a new Connection instance.
     *
     * @param session The underlying WebSocket session.
     * @param hook    A hook to be called when the connection is disconnected.
     */
    public Connection(Session session, DisconnectionHook hook)
    {
        this.connectionID = UUID.randomUUID().toString();
        this.session = session;
        this.disconnectionHook = hook;
    }


    /**
     * Returns the unique ID of the connection.
     *
     * @return The unique ID of the connection.
     */
    @Override
    public String getConnectionID()
    {
        return connectionID;
    }


    /**
     * Returns the current status of the connection.
     *
     * @return The current status of the connection.
     */
    @Override
    public ConnectionStatus getConnectionStatus()
    {
        return connectionStatus;
    }


    /**
     * Returns whether the connection is authenticated.
     *
     * @return True if the connection is authenticated, false otherwise.
     */
    @Override
    public abstract boolean isAuthenticated();


    /**
     * Returns whether the connection is still alive.
     *
     * @return True if the connection is still alive, false otherwise.
     */
    @Override
    public boolean isAlive()
    {
        return session.isOpen();
    }


    /**
     * Returns the IP address of the remote endpoint of the connection.
     *
     * @return The IP address of the remote endpoint of the connection.
     */
    @Override
    public String getConnectionAddress()
    {
        InetSocketAddress remoteAddress = session.getRemoteAddress();
        return remoteAddress.getAddress().getHostAddress();
    }


    /**
     * Disconnects the connection with no status code and no reason (Jetty will use something default).
     */
    @Override
    public void disconnect()
    {
        session.close();
        connectionStatus = ConnectionStatus.NOT_CONNECTED;
        disconnectionHook.onDisconnect(getSession());
    }


    /**
     * Disconnects the connection with the specified status code and reason.
     *
     * @param statusCode The status code to send in the close frame.
     * @param reason     The reason for disconnecting.
     */
    @Override
    public void disconnect(int statusCode, String reason)
    {
        session.close(statusCode, reason);
        connectionStatus = ConnectionStatus.NOT_CONNECTED;
        disconnectionHook.onDisconnect(getSession());
    }


    /**
     * Checks whether the connection is still alive and throws a ConnectException if not.
     *
     * @throws ConnectException If the connection is not alive.
     */
    protected void checkConnectionAlive() throws ConnectException
    {
        if (!isAlive())
        {
            throw new ConnectException("Not connected to edge router");
        }
    }


    /**
     * Returns the underlying WebSocket session.
     *
     * @return The underlying WebSocket session.
     */
    protected Session getSession()
    {
        return session;
    }
}