package SMMPClient.Acks;

import java.util.List;

/**
 * Just a simple class to hold the results of a delivery.
 */
public class DeliveryResult
{
    private final String messageId;
    private final List<String> ackedDestinations;
    private final List<String> failedDestinations;


    /**
     * Create a new delivery result.
     *
     * @param messageId          The message id of the message that was delivered.
     * @param ackedDestinations  The destinations that acknowledged the message.
     * @param failedDestinations The destinations that failed to acknowledge the message.
     */
    public DeliveryResult(String messageId, List<String> ackedDestinations, List<String> failedDestinations)
    {
        this.messageId = messageId;
        this.ackedDestinations = ackedDestinations;
        this.failedDestinations = failedDestinations;
    }


    /**
     * Get the message id of the message that was delivered.
     *
     * @return The message id.
     */
    public String getMessageId()
    {
        return messageId;
    }


    /**
     * Get the destinations that acknowledged the message.
     *
     * @return The destinations that acknowledged the message.
     */
    public List<String> getAckedDestinations()
    {
        return ackedDestinations;
    }


    /**
     * Get the destinations that failed to acknowledge the message.
     *
     * @return The destinations that failed to acknowledge the message.
     */
    public List<String> getFailedDestinations()
    {
        return failedDestinations;
    }
}
