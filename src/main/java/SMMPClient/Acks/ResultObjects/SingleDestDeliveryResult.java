package SMMPClient.Acks.ResultObjects;

/**
 * Represents the delivery result for a single destination acknowledgement.
 */
public class SingleDestDeliveryResult
{
    private final String destination;
    private final boolean acknowledged;


    /**
     * Constructs a new {@link SingleDestDeliveryResult} instance.
     *
     * @param destination  the destination of the message
     * @param acknowledged a boolean indicating whether the message was acknowledged or not
     */
    public SingleDestDeliveryResult(String destination, boolean acknowledged)
    {
        this.destination = destination;
        this.acknowledged = acknowledged;
    }


    /**
     * Returns the destination of the message.
     *
     * @return the destination as a {@link String}
     */
    public String getDestination()
    {
        return destination;
    }


    /**
     * Returns whether the message was acknowledged or not.
     *
     * @return a boolean indicating if the message was acknowledged
     */
    public boolean isAcknowledged()
    {
        return acknowledged;
    }
}
