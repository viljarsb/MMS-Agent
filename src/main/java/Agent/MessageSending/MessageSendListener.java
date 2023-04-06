package Agent.MessageSending;

/**
 * A listener for MMTP message send events.
 */
public interface MessageSendListener
{

    /**
     * Called when a message has been successfully sent.
     *
     * @param messageID The ID of the message that was sent.
     */
    void onSuccess(String messageID);

    /**
     * Called when a message sending has failed.
     *
     * @param ex The throwable instance that caused the sending failure.
     */
    void onFailure(Throwable ex);
}
