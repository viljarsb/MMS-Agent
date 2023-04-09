package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.DeliveryResult;
import SMMPClient.Acks.Listeners.MultiDeliveryCompletionHandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DefaultMultiDestCompletionHandler implements MultiDeliveryCompletionHandler
{
    private final CompletableFuture<DeliveryResult> future;

    public DefaultMultiDestCompletionHandler(CompletableFuture<DeliveryResult> future)
    {
        this.future = future;
    }

    @Override
    public void onAck(String destination)
    {
        //ignore
    }


    @Override
    public void onFullyAcked(List<String> destinations)
    {
        DeliveryResult result = new DeliveryResult(destinations, Collections.emptyList());
        future.complete(result);
    }


    @Override
    public void onTimeout(List<String> unacknowledged, List<String> acknowledged)
    {
        DeliveryResult result = new DeliveryResult(acknowledged, unacknowledged);
        future.complete(result);
    }


    @Override
    public void onFailure(Throwable t)
    {
        future.completeExceptionally(t);
    }
}
