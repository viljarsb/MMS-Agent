package SMMPClient.Acks.Listeners;

/**
 * A delivery listener is used to notify the user when a message has been delivered to a destination, when a timeout or a failure occurs
 */
public interface DeliveryListener
{
    void onAck();
    void onTimeout();
    void onFailed(Throwable t);
}
