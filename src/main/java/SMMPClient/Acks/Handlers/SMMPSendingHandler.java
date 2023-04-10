package SMMPClient.Acks.Handlers;

/**
 * A send listener is used to notify the user when a message has been sent.
 */
public interface SMMPSendingHandler
{
    /**
     * Called when a message has been sent successfully.
     */
    void onSuccess();

    /**
     * Called when a message sending failure occurs.
     *
     * @param ex The exception representing the failure
     */
    void onFailure(Throwable ex);
}
