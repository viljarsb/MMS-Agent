package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.Listeners.DeliveryListener;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * An entry in the ack table. This is used to track the status of a single-destination message.
 */
public class SingleDestAckEntry extends AckEntry
{
    private final String destination;
    private final byte[] message;
    private boolean acknowledged = false;
    private final DeliveryListener listener;
    private final CompletableFuture<Boolean> future;


    /**
     * Create a new single-destination ack entry
     *
     * @param messageId   The message id
     * @param destination The destination
     * @param message     The message
     * @param expires     The expiry time
     * @param listener    The listener to notify when the message has been delivered
     */
    public SingleDestAckEntry(String messageId, String destination, byte[] message, Instant expires, DeliveryListener listener)
    {
        super(messageId, expires);
        this.destination = destination;
        this.message = message;
        this.listener = listener;
        this.future = null;
    }


    /**
     * Create a new single-destination ack entry
     *
     * @param messageId   The message id
     * @param destination The destination
     * @param message     The message
     * @param expires     The expiry time
     * @param future      The future to complete when the message has been delivered
     */
    public SingleDestAckEntry(String messageId, String destination, byte[] message, Instant expires, CompletableFuture<Boolean> future)
    {
        super(messageId, expires);
        this.destination = destination;
        this.message = message;
        this.listener = null;
        this.future = future;
    }


    /**
     * Get the destination
     *
     * @return The destination
     */
    public String getDestination()
    {
        return destination;
    }


    /**
     * Get the message
     * @return The message
     */
    public byte[] getMessage()
    {
        return message;
    }

    /**
     * Acknowledge the message
     */
    @Override
    public void acknowledge(String destination)
    {
        acknowledged = true;

        if (future != null)
        {
            future.complete(true);
        }

        else if (listener != null)
        {
            listener.onAck();
        }
    }


    /**
     * Reject the message
     */
    @Override
    public void timeout()
    {
        if (future != null)
        {
            future.complete(false);
        }

        else if (listener != null)
        {
            listener.onTimeout();
        }
    }


    public List<String> getDestinations()
    {
        return Collections.singletonList(destination);
    }


    @Override
    public boolean isFullyAcknowledged()
    {
        return acknowledged;
    }
}
