package SMMPClient.Acks.AckEntry.DefaultEntryHandlers;

import SMMPClient.Acks.Handlers.SingleDeliveryCompletionHandler;
import SMMPClient.Acks.ResultObjects.SingleDestDeliveryResult;

import java.util.concurrent.CompletableFuture;

/**
 * Default implementation of {@link SingleDeliveryCompletionHandler} for single-destination message delivery.
 * Used for cases where user wants to use future based API over handler.
 */
public class DefaultSingleDestCompletionHandler implements SingleDeliveryCompletionHandler
{
    private final CompletableFuture<SingleDestDeliveryResult> future;


    /**
     * Constructs a new {@link DefaultSingleDestCompletionHandler} instance.
     *
     * @param future A CompletableFuture to hold the delivery result
     */
    public DefaultSingleDestCompletionHandler(CompletableFuture<SingleDestDeliveryResult> future)
    {
        this.future = future;
    }


    /**
     * ignore
     */
    @Override
    public void onAcked(String destination)
    {
        SingleDestDeliveryResult deliveryResult = new SingleDestDeliveryResult(destination, true);
        future.complete(deliveryResult);
    }


    /**
     * Called when a timeout occurs for a message that has not been acknowledged by the destination.
     */
    @Override
    public void onTimeout(String destination)
    {
        SingleDestDeliveryResult deliveryResult = new SingleDestDeliveryResult(destination, false);
        future.complete(deliveryResult);
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
