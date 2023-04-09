package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.Listeners.MultiDeliveryCompletionHandler;

import java.time.Instant;
import java.util.*;

public class MultiDestAckEntry extends AckEntry<MultiDeliveryCompletionHandler>
{
    private final List<String> destinations;
    private final Set<String> acknowledgedDestinations = new HashSet<>();


    public MultiDestAckEntry(String messageId, List<String> destinations, byte[] message, Instant expires, MultiDeliveryCompletionHandler handler)
    {
        super(messageId, message, expires, handler);
        this.destinations = destinations;
    }


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


    @Override
    public synchronized boolean isFullyAcknowledged()
    {
        return acknowledgedDestinations.size() == destinations.size();
    }


    @Override
    public synchronized List<String> getUnacknowledgedDestinations()
    {
        List<String> unacknowledgedDestinations = new ArrayList<>(destinations);
        unacknowledgedDestinations.removeAll(acknowledgedDestinations);
        return unacknowledgedDestinations;
    }


    @Override
    public List<String> getAcknowledgedDestinations()
    {
        return new ArrayList<>(acknowledgedDestinations);
    }



    @Override
    public void timeout()
    {
        handler.onTimeout(getUnacknowledgedDestinations(), getAcknowledgedDestinations());
    }


    @Override
    public MultiDeliveryCompletionHandler getHandler()
    {
        return handler;
    }
}