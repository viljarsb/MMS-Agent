package SMMPClient.Acks.Handlers;


/**
 * Interface for handling single-destination message delivery completion events.
 */
public interface SingleDeliveryCompletionHandler
{
    /**
     * Called when a message has been acknowledged by the destination.
     *
     * @param destination the destination that acked this message
     */
    void onAcked(String destination);

    /**
     * Called when a timeout occurs for a message that has not been acknowledged by the destination.
     *
     * @param destination the destination that acked this message
     */
    void onTimeout(String destination);

    /**
     * Called when a timeout occurs for a message that has not been acknowledged by the destination.
     */
    void onFailure(Throwable t);
}
