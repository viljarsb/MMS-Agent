package SMMPClient.Acks.Listeners;

/**
 * A send listener is used to notify the user when a message has been sent.
 */
public interface SMMPSendingListener
{
    void onSuccess();
    void onFailure(Throwable ex);
}
