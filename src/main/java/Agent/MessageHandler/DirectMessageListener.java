package Agent.MessageHandler;

import java.time.Instant;
import java.util.List;

/**
 * Interface for handling direct application messages.
 */
public interface DirectMessageListener extends SubjectMessageListener
{
    /**
     * Called when a direct application message is received.
     *
     * @param messageId    the ID of the message
     * @param destinations the destinations of the message
     * @param sender       the sender of the message
     * @param expires      the expiration time of the message
     * @param message      the message content
     */
    void onDirectMessage(String messageId, List<String> destinations, String sender, Instant expires, byte[] message);
}
