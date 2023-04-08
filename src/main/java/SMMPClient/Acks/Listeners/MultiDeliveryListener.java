package SMMPClient.Acks.Listeners;

import java.util.List;

/**
 * A multi delivery listener is used to notify the user when a message has been delivered to all destinations, when a timeout or a failure occurs
 */
public interface MultiDeliveryListener
{
    void onAllAck();
    void onTimeout(List<String> acked, List<String> unacked);
    void onFailed(Throwable t);
}
