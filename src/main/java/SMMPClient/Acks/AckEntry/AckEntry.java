package SMMPClient.Acks.AckEntry;

import lombok.NonNull;

import java.time.Instant;
import java.util.List;


/**
 * Represents an entry in a message that is waiting for acknowledgement.
 * Abstract class that is extended by the different types of ack entries.
 *
 * @param <T> The type of the handler that is used to handle the ack entry
 */
public abstract class AckEntry<T>
{
    protected final String messageId;
    protected final byte[] message;
    protected final Instant expires;
    protected final T handler;


    /**
     * Constructor for {@link AckEntry<T>} construct a new instance of {@link AckEntry<T>}
     *
     * @param messageId the SMMP-message id of the message to track
     * @param message   the message to resend in case of missing acks
     * @param expires   the expiry of the message
     * @param handler   the handler that should be used as a callback
     */
    public AckEntry(@NonNull String messageId, @NonNull byte[] message, Instant expires, @NonNull T handler)
    {
        this.messageId = messageId;
        this.message = message;
        this.expires = expires;
        this.handler = handler;
    }


    /**
     * Gets the SMMP-message id that this entry is tracking.
     *
     * @return the SMMP-message id that this entry tracks
     */
    public final String getMessageId()
    {
        return messageId;
    }


    /**
     * Gets the expiry time of the message that this entry is tracking.
     *
     * @return the expiry time of this entry
     */
    public final Instant getExpires()
    {
        return expires;
    }


    /**
     * Gets the SMMP-message that this entry contains.
     *
     * @return the SMMP-message that this entry contains
     */
    public final byte[] getMessage()
    {
        return message;
    }


    /**
     * Acknowledges this entry.
     *
     * @param destination The sender that has acknowledged this entry
     * @return true if ack status changed
     */
    public abstract boolean acknowledge(String destination);


    /**
     * Checks if this entry if fully acknowledged.
     *
     * @return true if fully acknowledged, false otherwise
     */
    public abstract boolean isFullyAcknowledged();


    /**
     * Gets all the destinations that has not acked this entry.
     *
     * @return a list of destinations that has not acked this entry
     */
    public abstract List<String> getUnacknowledgedDestinations();


    /**
     * Gets all the destinations that has acknowledged this entry.
     *
     * @return a list of destinations that has acknowledged this entry
     */
    public abstract List<String> getAcknowledgedDestinations();


    /**
     * Called once this entry is reaches its max timeout.
     */
    public abstract void timeout();


    /**
     * Returns the handler associated with this entry.
     *
     * @return the handler of this entry
     */
    public abstract T getHandler();
}
