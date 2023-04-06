package Agent.WebSocket;

import Agent.Connections.IConnection;

public interface IConnectionListener
{
    void onConnect(IConnection connection);
    void onDisconnect(int statusCode, String reason);
    void onConnectionError(Throwable ex);
}
