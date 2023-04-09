package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.Listeners.SingleDeliveryCompletionHandler;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class SingleDestAckEntry extends AckEntry<SingleDeliveryCompletionHandler>
{
    private final String destination;
    private boolean acknowledged = false;


    public SingleDestAckEntry(String messageId, String destination, byte[] message, Instant expires, SingleDeliveryCompletionHandler handler)
    {
        super(messageId, message, expires, handler);
        this.destination = destination;
    }


    @Override
    public synchronized boolean acknowledge(String destination)
    {
        if (this.destination.equals(destination))
        {
            acknowledged = true;
            handler.onAcked();
            return true;
        }

        return false;
    }


    @Override
    public synchronized boolean isFullyAcknowledged()
    {
        return acknowledged;
    }


    @Override
    public synchronized List<String> getUnacknowledgedDestinations()
    {
        return acknowledged ? Collections.emptyList() : Collections.singletonList(destination);
    }


    @Override
    public List<String> getAcknowledgedDestinations()
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


    @Override
    public void timeout()
    {
        handler.onTimeout();
    }


    @Override
    public SingleDeliveryCompletionHandler getHandler()
    {
        return handler;
    }
}

