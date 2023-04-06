package Agent.MessageHandler;

import java.time.Instant;

/**
 * Interface for handling subject cast application messages.
 */
public interface SubjectMessageListener
{
    /**
     * Called when a subject-cast message is received.
     *
     * @param messageId the ID of the message
     * @param sender    the sender of the message
     * @param subject   the subject of the message
     * @param expires   the expiration time of the message
     * @param message   the message content
     */
    void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message);
}
