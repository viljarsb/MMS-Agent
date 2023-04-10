package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.Handlers.MultiDeliveryCompletionHandler;
import lombok.NonNull;

import java.time.Instant;
import java.util.*;


/**
 * Represents a message to multiple destinations.
 * Extends the {@link AckEntry<MultiDeliveryCompletionHandler>}.
 * Tracks the progress of acks for each destination.
 */
public class MultiDestAckEntry extends AckEntry<MultiDeliveryCompletionHandler>
{
    private final List<String> destinations;
    private final Set<String> acknowledgedDestinations = new HashSet<>();


    /**
     * Constructs a new {@link MultiDestAckEntry} instance.
     *
     * @param messageId    the message id that this entry tracks
     * @param destinations the destinations that this message is sent to
     * @param message      the message to resend in case of missing acks
     * @param expires      the expiry of the message
     * @param handler      the multi delivery completion handler that should be used as a callback
     */
    public MultiDestAckEntry(@NonNull String messageId, @NonNull List<String> destinations, @NonNull byte[] message, Instant expires, @NonNull MultiDeliveryCompletionHandler handler)
    {
        super(messageId, message, expires, handler);
        this.destinations = destinations;
    }


    /**
     * Acknowledges this message. Checks if the destination matches a missing ack.
     * Inform the callback of the ack, if fully acknowledged, inform the callback of fully acked status.
     *
     * @param destination The sender that has acknowledged this entry
     * @return true if the ack status changed, false otherwise
     */
    @Override
    public synchronized boolean acknowledge(String destination)
    {
        if (destinations.contains(destination))
        {
            acknowledgedDestinations.add(destination);
            handler.onAck(destination);
            if (acknowledgedDestinations.size() == destinations.size())
            {
                handler.onFullyAcked(destinations);
            }

            return true;
        }

        return false;
    }


    /**
     * Checks if all destinations has acknowledged this entry.
     *
     * @return true if all destinations has acked the entry, false otherwise
     */
    @Override
    public synchronized boolean isFullyAcknowledged()
    {
        return acknowledgedDestinations.size() == destinations.size();
    }


    /**
     * Gets all the destinations that has not acknowledged this entry.
     *
     * @return a list of all destinations that has not acknowledged this entry.
     */
    @Override
    public synchronized List<String> getUnacknowledgedDestinations()
    {
        List<String> unacknowledgedDestinations = new ArrayList<>(destinations);
        unacknowledgedDestinations.removeAll(acknowledgedDestinations);
        return unacknowledgedDestinations;
    }

    /**
     * Gets all the destinations that have acknowledged this entry.
     *
     * @return a list of all destinations that have acknowledged this entry.
     */
    @Override
    public synchronized List<String> getAcknowledgedDestinations()
    {
        return new ArrayList<>(acknowledgedDestinations);
    }


    /**
     * Called once this entry reaches it max timeout, informs the callback that the timeout has occurred.
     * The callback is supplied a list of destinations that acknowledged and destinations that did not.
     */
    @Override
    public synchronized void timeout()
    {
        handler.onTimeout(getUnacknowledgedDestinations(), getAcknowledgedDestinations());
    }


    /**
     * Gets the handler associated with this entry.
     * @return the multi delivery completion handler associated with this entry.
     */
    @Override
    public MultiDeliveryCompletionHandler getHandler()
    {
        return handler;
    }
}