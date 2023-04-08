package SMMPClient.Acks.AckEntry;

import java.time.Instant;


/**
 * An entry in the ack table. This is used to track the status of a message.
 * Abstract class, use one of the subclasses.
 */
public abstract class AckEntry
{
    private final String messageId;
    private final Instant expires;


    public AckEntry(String messageId, Instant expires)
    {
        this.messageId = messageId;
        this.expires = expires;
    }


    public final Instant getExpires()
    {
        return expires;
    }

    public final String getMessageId()
    {
        return messageId;
    }

    public abstract void timeout();

    public abstract void acknowledge(String destination);

    public abstract boolean isFullyAcknowledged();
}
