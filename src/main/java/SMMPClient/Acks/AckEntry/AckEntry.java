package SMMPClient.Acks.AckEntry;

import java.time.Instant;
import java.util.List;

public abstract class AckEntry<T>
{
    protected final String messageId;
    protected final byte[] message;
    protected final Instant expires;
    protected final T handler;


    public AckEntry(String messageId, byte[] message, Instant expires, T handler)
    {
        this.messageId = messageId;
        this.message = message;
        this.expires = expires;
        this.handler = handler;
    }


    public final String getMessageId()
    {
        return messageId;
    }


    public final Instant getExpires()
    {
        return expires;
    }


    public final byte[] getMessage()
    {
        return message;
    }


    public abstract boolean acknowledge(String destination);

    public abstract boolean isFullyAcknowledged();

    public abstract List<String> getUnacknowledgedDestinations();

    public abstract List<String> getAcknowledgedDestinations();

    public abstract void timeout();

    public abstract T getHandler();
}
