package SMMPClient.Acks;

import SMMPClient.Acks.AckEntry.*;
import SMMPClient.Acks.Listeners.MultiDeliveryCompletionHandler;
import SMMPClient.Acks.Listeners.SingleDeliveryCompletionHandler;
import SMMPClient.Acks.Listeners.SubjectDeliveryCompletionListener;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class AckTracker
{
    private static final int INITIAL_DELAY = 5;
    private static final int MAX_DELAY = 600;
    private static final TimeUnit DELAY_UNIT = TimeUnit.SECONDS;
    private static final int MAX_THREADS = 3;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(MAX_THREADS);
    private final Map<String, AckEntry<?>> acks = new ConcurrentHashMap<>();
    private final ResendHook resendHook;

    public AckTracker(ResendHook resendHook)
    {
        this.resendHook = resendHook;
    }

    public void waitForSingleDestAck(String messageId, String destination, byte[] message, Instant expires, ResendHook resendHook, SingleDeliveryCompletionHandler handler)
    {
        SingleDestAckEntry entry = new SingleDestAckEntry(messageId, destination, message, expires, handler);
        acks.put(messageId, entry);
        executor.schedule(() -> checkAndRetransmit(entry, INITIAL_DELAY, MAX_DELAY, resendHook), INITIAL_DELAY, DELAY_UNIT);
    }


    public CompletableFuture<Boolean> waitForSingleDestAck(String messageId, String destination, byte[] message, Instant expires, ResendHook resendHook)
    {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DefaultSingleDestCompletionHandler handler = new DefaultSingleDestCompletionHandler(future);

        SingleDestAckEntry entry = new SingleDestAckEntry(messageId, destination, message, expires, handler);
        acks.put(messageId, entry);
        executor.schedule(() -> checkAndRetransmit(entry, 5, 600, resendHook), 5, TimeUnit.SECONDS);
        return future;
    }


    public void waitForMultiDestAck(String messageId, List<String> destinations, byte[] message, Instant expires, ResendHook resendHook, MultiDeliveryCompletionHandler handler)
    {
        MultiDestAckEntry entry = new MultiDestAckEntry(messageId, destinations, message, expires, handler);
        acks.put(messageId, entry);
        executor.schedule(() -> checkAndRetransmit(entry, 5, 600, resendHook), 5, TimeUnit.SECONDS);
    }


    public CompletableFuture<DeliveryResult> waitForMultiDestAck(String messageId, List<String> destinations, byte[] message, Instant expires, ResendHook resendHook)
    {
        CompletableFuture<DeliveryResult> future = new CompletableFuture<>();
        DefaultMultiDestCompletionHandler handler = new DefaultMultiDestCompletionHandler(future);

        MultiDestAckEntry entry = new MultiDestAckEntry(messageId, destinations, message, expires, handler);
        acks.put(messageId, entry);
        executor.schedule(() -> checkAndRetransmit(entry, 5, 600, resendHook), 5, TimeUnit.SECONDS);
        return future;
    }


    public void waitForSubjectCastAck(String messageId, Instant expires, SubjectDeliveryCompletionListener handler)
    {
        SubjectDestAckEntry entry = new SubjectDestAckEntry(messageId, null, expires, handler);
        acks.put(messageId, entry);

        if (expires != null)
        {
            Instant now = Instant.now();
            long secondsUntilTarget = Duration.between(now, expires).getSeconds();
            executor.schedule(() ->
            {
                acks.remove(messageId);
                entry.timeout();
            }, secondsUntilTarget, TimeUnit.SECONDS);
        }

        else
        {
            executor.schedule(() ->
            {
                acks.remove(messageId);
                entry.timeout();
            }, 10, TimeUnit.SECONDS);
        }
    }


    public CompletableFuture<DeliveryResultSubjectCast> waitForSubjectCastAck(String messageId, Instant expires)
    {
        CompletableFuture<DeliveryResultSubjectCast> future = new CompletableFuture<>();
        DefaultSubjectCastDestCompletionHandler handler = new DefaultSubjectCastDestCompletionHandler(future);

        SubjectDestAckEntry entry = new SubjectDestAckEntry(messageId, null, expires, handler);
        acks.put(messageId, entry);

        if (expires != null)
        {
            Instant now = Instant.now();
            long secondsUntilTarget = Duration.between(now, expires).getSeconds();
            executor.schedule(() ->
            {
                acks.remove(messageId);
                entry.timeout();
            }, secondsUntilTarget, TimeUnit.SECONDS);
        }

        else
        {
            executor.schedule(() ->
            {
                acks.remove(messageId);
                entry.timeout();
            }, 10, TimeUnit.SECONDS);
        }

        return future;
    }


    public void acknowledge(String messageId, String destination)
    {
        AckEntry<?> entry = acks.get(messageId);

        if (entry != null)
        {
            boolean acked = entry.acknowledge(destination);
        }
    }


    private void checkAndRetransmit(AckEntry<?> entry, int delay, int maxDelay, ResendHook resendHook)
    {
        if (entry.isFullyAcknowledged())
        {
            acks.remove(entry.getMessageId());
            return;
        }

        if (delay < maxDelay && entry.getExpires().isAfter(Instant.now()))
        {
            List<String> unacknowledgedDestinations = entry.getUnacknowledgedDestinations();
            byte[] message = entry.getMessage();
            Instant expires = entry.getExpires();
            resendHook.resend(unacknowledgedDestinations, message, expires);
            executor.schedule(() -> checkAndRetransmit(entry, delay * 2, maxDelay, resendHook), delay, TimeUnit.SECONDS);
        }

        else
        {
            acks.remove(entry.getMessageId());
            entry.timeout();
        }
    }
}