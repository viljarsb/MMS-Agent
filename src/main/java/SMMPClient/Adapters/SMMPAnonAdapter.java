package SMMPClient.Adapters;

import SMMPClient.Connections.SMMPAnonConnection;

import java.time.Instant;

/**
 * The interface that users of the SMMP client library must implement.
 */
public interface SMMPAnonAdapter
{
    void onConnect(SMMPAnonConnection connection);

    void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message);

    void onDisconnect(int statusCode, String reason);

    void onConnectionError(Throwable ex);
}
