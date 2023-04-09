package SMMPClient.Acks;


import java.time.Instant;
import java.util.List;

/**
 * A resend hook is used to resend messages that have not been acknowledged.
 */
public interface ResendHook
{
    void resend(List<String> destinations, byte[] message, Instant expires);
    void ackResponse(String messageId, String destination);
}
