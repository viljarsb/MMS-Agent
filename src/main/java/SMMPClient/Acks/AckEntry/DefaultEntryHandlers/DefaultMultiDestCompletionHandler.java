package SMMPClient.Acks.AckEntry.DefaultEntryHandlers;

import SMMPClient.Acks.ResultObjects.MultiDestDeliveryResult;
import SMMPClient.Acks.Handlers.MultiDeliveryCompletionHandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Default implementation of {@link MultiDeliveryCompletionHandler} for multi-destination message delivery.
 * Used for cases where user wants to use future based API over handler.
 */
public class DefaultMultiDestCompletionHandler implements MultiDeliveryCompletionHandler
{
    private final CompletableFuture<MultiDestDeliveryResult> future;


    /**
     * Constructs a new {@link DefaultMultiDestCompletionHandler} instance.
     *
     * @param future A CompletableFuture to hold the delivery result
     */
    public DefaultMultiDestCompletionHandler(CompletableFuture<MultiDestDeliveryResult> future)
    {
        this.future = future;
    }


    /**
     * ignored.
     *
     * @param destination ignored.
     */
    @Override
    public void onAck(String destination)
    {
        //ignore
    }


    /**
     * Called when a message has been fully acknowledged by all destinations.
     *
     * @param destinations The list of destinations that acknowledged the message
     */
    @Override
    public void onFullyAcked(List<String> destinations)
    {
        MultiDestDeliveryResult result = new MultiDestDeliveryResult(destinations, Collections.emptyList());
        future.complete(result);
    }


    /**
     * Called when a timeout occurs for a message that has not been acknowledged by all destinations.
     *
     * @param unacknowledged The list of destinations that did not acknowledge the message
     * @param acknowledged   The list of destinations that acknowledged the message
     */
    @Override
    public void onTimeout(List<String> unacknowledged, List<String> acknowledged)
    {
        MultiDestDeliveryResult result = new MultiDestDeliveryResult(acknowledged, unacknowledged);
        future.complete(result);
    }


    /**
     * Called when a message sending failure occurs.
     *
     * @param t The throwable representing the failure
     */
    @Override
    public void onFailure(Throwable t)
    {
        future.completeExceptionally(t);
    }
}
