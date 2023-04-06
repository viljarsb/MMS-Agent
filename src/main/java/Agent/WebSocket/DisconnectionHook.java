package Agent.WebSocket;

import org.eclipse.jetty.websocket.api.Session;

public interface DisconnectionHook
{
    void onDisconnect(Session session);
}
