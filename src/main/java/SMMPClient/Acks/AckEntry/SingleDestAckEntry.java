package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.Handlers.SingleDeliveryCompletionHandler;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Represents a message to a single destination.
 * Extends the {@link AckEntry<SingleDeliveryCompletionHandler>}.
 * Tracks the progress of ack for the destination.
 */
public class SingleDestAckEntry extends AckEntry<SingleDeliveryCompletionHandler>
{
    private final String destination;
    private boolean acknowledged = false;


    /**
     * Constructs a new {@link SingleDestAckEntry} instance.
     *
     * @param messageId   the message id that this entry tracks
     * @param destination the destination that this message is sent to
     * @param message     the message to resend in case of missing acks
     * @param expires     the expiry of the message
     * @param handler     the single delivery completion handler that should be used as a callback
     */
    public SingleDestAckEntry(String messageId, String destination, byte[] message, Instant expires, SingleDeliveryCompletionHandler handler)
    {
        super(messageId, message, expires, handler);
        this.destination = destination;
    }


    /**
     * Acknowledges this message. Checks if the destination matches the single destination.
     * Inform the callback of the ack if the destination matches.
     *
     * @param destination The sender that has acknowledged this entry
     * @return true if the ack status changed, false otherwise
     */
    @Override
    public synchronized boolean acknowledge(String destination)
    {
        if (this.destination.equals(destination))
        {
            acknowledged = true;
            handler.onAcked(destination);
            return true;
        }

        return false;
    }


    /**
     * Checks if the single destination has acknowledged this entry.
     *
     * @return true if the destination has acked the entry, false otherwise
     */
    @Override
    public synchronized boolean isFullyAcknowledged()
    {
        return acknowledged;
    }


    /**
     * Gets the destination that has not acknowledged this entry.
     *
     * @return an empty list if the destination has acknowledged, or a list containing the destination if it has not acknowledged this entry
     */
    @Override
    public synchronized List<String> getUnacknowledgedDestinations()
    {
        return acknowledged ? Collections.emptyList() : Collections.singletonList(destination);
    }


    /**
     * Gets the destination that has acknowledged this entry.
     *
     * @return a list containing the destination if it has acknowledged this entry, or an empty list otherwise
     */
    @Override
    public synchronized List<String> getAcknowledgedDestinations()
    {
        if (acknowledged)
        {
            return Collections.singletonList(destination);
        }
        else
        {
            return Collections.emptyList();
        }
    }


    /**
     * Called once this entry reaches its max timeout, informs the callback that the timeout has occurred.
     */
    @Override
    public synchronized void timeout()
    {
        handler.onTimeout(destination);
    }


    /**
     * Gets the handler associated with this entry.
     *
     * @return the single delivery completion handler associated with this entry.
     */
    @Override
    public SingleDeliveryCompletionHandler getHandler()
    {
        return handler;
    }
}

