package SMMPClient.Acks;

import SMMPClient.Acks.Handlers.MultiDeliveryCompletionHandler;
import SMMPClient.Acks.Handlers.SingleDeliveryCompletionHandler;
import SMMPClient.Acks.ResultObjects.MultiDestDeliveryResult;
import SMMPClient.Acks.ResultObjects.SingleDestDeliveryResult;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface that acknowledgement trackers should implement.
 * Provides methods to wait acks and to send acks.
 */
public interface IAckTracker
{
    void waitForSingleDestAck(@NonNull String messageId, @NonNull String destination, @NonNull byte[] message, Instant expires, @NonNull SingleDeliveryCompletionHandler handler);

    CompletableFuture<SingleDestDeliveryResult> waitForSingleDestAck(@NonNull String messageId, @NonNull String destination, @NonNull byte[] message, Instant expires);

    void waitForMultiDestAck(@NonNull String messageId, @NonNull List<String> destinations, @NonNull byte[] message, Instant expires, @NonNull MultiDeliveryCompletionHandler handler);

    CompletableFuture<MultiDestDeliveryResult> waitForMultiDestAck(@NonNull String messageId, @NonNull List<String> destinations, @NonNull byte[] message, Instant expires);

    void acknowledge(@NonNull String messageId, @NonNull String destination);

    void sendAck(@NonNull String messageId, @NonNull String destination);
}