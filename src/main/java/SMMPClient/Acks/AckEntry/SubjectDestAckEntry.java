package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.Listeners.SubjectDeliveryCompletionListener;

import java.time.Instant;
import java.util.*;

public class SubjectDestAckEntry extends AckEntry<SubjectDeliveryCompletionListener>
{
    private final Set<String> acknowledgedDestinations = new HashSet<>();


    public SubjectDestAckEntry(String messageId, byte[] message, Instant expires, SubjectDeliveryCompletionListener handler)
    {
        super(messageId, message, expires, handler);
    }



    @Override
    public boolean acknowledge(String destination)
    {
        boolean changed = acknowledgedDestinations.add(destination);

        if (changed)
        {
            handler.onAck(destination);
            return true;
        }

        return false;
    }


    @Override
    public boolean isFullyAcknowledged()
    {
        return false;
    }


    @Override
    public List<String> getUnacknowledgedDestinations()
    {
        return Collections.emptyList();
    }


    @Override
    public List<String> getAcknowledgedDestinations()
    {
        return new ArrayList<>(acknowledgedDestinations);
    }


    @Override
    public void timeout()
    {
        handler.onExpired(new ArrayList<>(acknowledgedDestinations));
    }


    @Override
    public SubjectDeliveryCompletionListener getHandler()
    {
        return handler;
    }
}
