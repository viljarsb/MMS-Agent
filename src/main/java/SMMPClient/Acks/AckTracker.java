package SMMPClient.Acks;

import SMMPClient.Acks.AckEntry.AckEntry;
import SMMPClient.Acks.AckEntry.MultiDestAckEntry;
import SMMPClient.Acks.AckEntry.SingleDestAckEntry;
import SMMPClient.Acks.Listeners.DeliveryListener;
import SMMPClient.Acks.Listeners.MultiDeliveryListener;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * A class used to track the acknowledgements of messages.
 * <p>
 * Accepts acknowledgements from the agent adapters when they receive an acknowledgement.
 * Notifies the listeners provided by the user when an acknowledgement is received or when
 * the message expires or a timeout occurs. Also resends messages that have not been
 * acknowledged within a certain time period, uses a resend hook to do this.
 */
public class AckTracker
{
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Map<String, AckEntry> acks = new ConcurrentHashMap<>();


    /**
     * Add a single destination acknowledgement to the tracker.
     *
     * @param messageId   The message id of the message that was sent.
     * @param destination The destination that the message was sent to.
     * @param message     The message that was sent.
     * @param expires     The time that the message expires.
     * @param resendHook  The resend hook to use to resend the message.
     * @param listener    The listener to notify when the message is acknowledged or expires.
     */
    public void waitForSingleDestAck(String messageId, String destination, byte[] message, Instant expires, ResendHook resendHook, DeliveryListener listener)
    {
        SingleDestAckEntry entry = new SingleDestAckEntry(messageId, destination, message, expires, listener);
        acks.put(messageId, entry);

        executor.schedule(() -> check(messageId, 5, 600, resendHook), 5, TimeUnit.SECONDS);
    }


    /**
     * Add a single destination acknowledgement to the tracker.
     *
     * @param messageId   The message id of the message that was sent.
     * @param destination The destination that the message was sent to.
     * @param message     The message that was sent.
     * @param expires     The time that the message expires.
     * @param resendHook  The resend hook to use to resend the message.
     * @return A future that will be completed when the message is acknowledged or expires.
     */
    public CompletableFuture<Boolean> waitForSingleDestAck(String messageId, String destination, byte[] message, Instant expires, ResendHook resendHook)
    {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        SingleDestAckEntry entry = new SingleDestAckEntry(messageId, destination, message, expires, future);
        acks.put(messageId, entry);

        executor.schedule(() -> check(messageId, 5, 600, resendHook), 5, TimeUnit.SECONDS);
        return future;
    }


    /**
     * Add a multi destination acknowledgement to the tracker.
     *
     * @param messageId    The message id of the message that was sent.
     * @param destinations The destinations that the message was sent to.
     * @param message      The message that was sent.
     * @param expires      The time that the message expires.
     * @param resendHook   The resend hook to use to resend the message.
     * @param listener     The listener to notify when the message is acknowledged or expires.
     */
    public void waitForMultiDestAck(String messageId, List<String> destinations, byte[] message, Instant expires, ResendHook resendHook, MultiDeliveryListener listener)
    {
        MultiDestAckEntry entry = new MultiDestAckEntry(messageId, destinations, message, expires, listener);
        acks.put(messageId, entry);

        executor.schedule(() -> check(messageId, 5, 600, resendHook), 5, TimeUnit.SECONDS);
    }


    /**
     * Add a multi destination acknowledgement to the tracker.
     *
     * @param messageId    The message id of the message that was sent.
     * @param destinations The destinations that the message was sent to.
     * @param message      The message that was sent.
     * @param expires      The time that the message expires.
     * @param resendHook   The resend hook to use to resend the message.
     * @return A future that will be completed when the message is acknowledged or expires.
     */
    public CompletableFuture<DeliveryResult> waitForMultiDestAck(String messageId, List<String> destinations, byte[] message, Instant expires, ResendHook resendHook)
    {
        CompletableFuture<DeliveryResult> future = new CompletableFuture<>();
        MultiDestAckEntry entry = new MultiDestAckEntry(messageId, destinations, message, expires, future);
        acks.put(messageId, entry);

        executor.schedule(() -> check(messageId, 5, 600, resendHook), 5, TimeUnit.SECONDS);
        return future;
    }


    /**
     * Acknowledge a message.
     *
     * @param messageID   The message id of the message that was acknowledged.
     * @param destination The destination that the message was acknowledged from.
     */
    public void acknowledge(String messageID, String destination)
    {
        AckEntry entry = acks.get(messageID);

        if (entry instanceof SingleDestAckEntry singleDestEntry)
        {
            singleDestEntry.acknowledge();
        }

        else if (entry instanceof MultiDestAckEntry multiDestAckEntry)
        {
            multiDestAckEntry.acknowledge(destination);
        }
    }


    /**
     * Check if a message has been acknowledged.
     *
     * @param messageID  The message id of the message to check.
     * @param delay      The delay to wait before checking again.
     * @param maxDelay   The maximum delay to wait before checking again.
     * @param resendHook The resend hook to use to resend the message.
     */
    private void check(String messageID, int delay, int maxDelay, ResendHook resendHook)
    {
        AckEntry entry = acks.get(messageID);

        if (entry.isFullyAcknowledged())
        {
            acks.remove(messageID);
            return;
        }

        if (entry instanceof SingleDestAckEntry singleDestEntry)
        {
            if (delay < maxDelay && entry.getExpires().isAfter(Instant.now()))
            {
                resendHook.resend(singleDestEntry.getDestination(), singleDestEntry.getMessage(), entry.getExpires());
                executor.schedule(() -> check(messageID, delay * 2, maxDelay, resendHook), delay, TimeUnit.SECONDS);
            }

            else
            {
                acks.remove(messageID);
                singleDestEntry.timeout();
            }
        }

        else if (entry instanceof MultiDestAckEntry multiDestAckEntry)
        {
            if (delay < maxDelay && entry.getExpires().isAfter(Instant.now()))
            {
                resendHook.resend(multiDestAckEntry.getUnacknowledgedDestinations(), multiDestAckEntry.getMessage(), entry.getExpires());
                executor.schedule(() -> check(messageID, delay * 2, maxDelay, resendHook), delay, TimeUnit.SECONDS);
            }

            else
            {
                acks.remove(messageID);
                multiDestAckEntry.timeout();
            }
        }
    }
}