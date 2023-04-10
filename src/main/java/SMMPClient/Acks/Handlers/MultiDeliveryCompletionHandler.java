package SMMPClient.Acks.Handlers;

import java.util.List;

/**
 * Interface for handling multi-destination message delivery completion events.
 */
public interface MultiDeliveryCompletionHandler
{
    /**
     * Called when a message has been acknowledged by a destination.
     *
     * @param destination The destination that acknowledged the message
     */
    void onAck(String destination);

    /**
     * Called when a message has been fully acknowledged by all destinations.
     *
     * @param destinations The list of destinations that acknowledged the message
     */
    void onFullyAcked(List<String> destinations);

    /**
     * Called when a timeout occurs for a message that has not been acknowledged by all destinations.
     *
     * @param unacknowledged The list of destinations that did not acknowledge the message
     * @param acknowledged   The list of destinations that acknowledged the message
     */
    void onTimeout(List<String> unacknowledged, List<String> acknowledged);

    /**
     * Called when a message sending failure occurs.
     *
     * @param t The throwable representing the failure
     */
    void onFailure(Throwable t);
}
