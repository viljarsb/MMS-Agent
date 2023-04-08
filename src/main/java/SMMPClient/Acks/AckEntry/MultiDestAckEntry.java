package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.DeliveryResult;
import SMMPClient.Acks.Listeners.MultiDeliveryListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * An entry in the ack table. This is used to track the status of a multi-destination message.
 * This class is used when the user wants to be notified when a message has been delivered to all destinations.
 */
public class MultiDestAckEntry extends AckEntry
{
    private final List<String> unacknowledgedDestinations;
    private final byte[] message;
    private final List<String> acknowledgedDestinations = new ArrayList<>();
    private final MultiDeliveryListener listener;
    private final CompletableFuture<DeliveryResult> future;


    /**
     * Create a new multi-destination ack entry
     *
     * @param messageId    The message id
     * @param destinations The destinations
     * @param message      The message
     * @param expires      The expiry time
     * @param listener     The listener to notify when the message has been delivered to all destinations
     */
    public MultiDestAckEntry(String messageId, List<String> destinations, byte[] message, Instant expires, MultiDeliveryListener listener)
    {
        super(messageId, expires);
        this.unacknowledgedDestinations = destinations;
        this.message = message;
        this.listener = listener;
        this.future = null;
    }


    /**
     * Create a new multi-destination ack entry
     *
     * @param messageId    The message id
     * @param destinations The destinations
     * @param message      The message
     * @param expires      The expiry time
     * @param future       The future to complete when the message has been delivered to all destinations
     */
    public MultiDestAckEntry(String messageId, List<String> destinations, byte[] message, Instant expires, CompletableFuture<DeliveryResult> future)
    {
        super(messageId, expires);
        this.unacknowledgedDestinations = destinations;
        this.message = message;
        this.listener = null;
        this.future = future;
    }



    /**
     * Gets the message that is being delivered.
     *
     * @return The message
     */
    public byte[] getMessage()
    {
        return message;
    }


    /**
     * Acknowledge a destination. This is called when a message has been delivered to a destination.
     * If all destinations have been acknowledged, the listener or future will be notified.
     *
     * @param destination The destination
     */
    public void acknowledge(String destination)
    {
        unacknowledgedDestinations.remove(destination);
        acknowledgedDestinations.add(destination);

        if (isFullyAcknowledged())
        {
            if (listener != null)
            {
                listener.onAllAck();
            }

            else if (future != null)
            {
                DeliveryResult result = new DeliveryResult(getMessageId(), getAcknowledgedDestinations(), getUnacknowledgedDestinations());
                assert future != null;
                future.complete(result);
            }
        }
    }


    /**
     * Gets the destinations that have been acknowledged.
     *
     * @return The destinations
     */
    public List<String> getAcknowledgedDestinations()
    {
        return acknowledgedDestinations;
    }


    /**
     * Gets the destinations that have not been acknowledged.
     *
     * @return The destinations
     */
    public List<String> getUnacknowledgedDestinations()
    {
        return unacknowledgedDestinations;
    }


    /**
     * Called when the message has timed out. This will notify the listener or future.
     */
    @Override
    public void timeout()
    {
        if (listener != null)
        {
            listener.onTimeout(getAcknowledgedDestinations(), getUnacknowledgedDestinations());
        }

        else if (future != null)
        {
            DeliveryResult result = new DeliveryResult(getMessageId(), getAcknowledgedDestinations(), getUnacknowledgedDestinations());
            assert future != null;
            future.complete(result);
        }
    }


    @Override
    public boolean isFullyAcknowledged()
    {
        return unacknowledgedDestinations.size() == 0;
    }
}
