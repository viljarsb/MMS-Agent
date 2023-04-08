package SMMPClient.Adapters;


import SMMPClient.Connections.SMMPAuthConnection;

import java.time.Instant;
import java.util.List;

/**
 * The interface that users of the SMMP client library must implement.
 */
public interface SMMPAuthAdapter
{
    void onConnect(SMMPAuthConnection connection);

    void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message);

    void onDirectMessage(String messageId, List<String> destinations, String sender, Instant expires, byte[] message);

    void onDisconnect(int statusCode, String reason);

    void onConnectionError(Throwable ex);
}
