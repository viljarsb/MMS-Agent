package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.Listeners.SingleDeliveryCompletionHandler;

import java.util.concurrent.CompletableFuture;

public class DefaultSingleDestCompletionHandler implements SingleDeliveryCompletionHandler
{
    private final CompletableFuture<Boolean> future;

    public DefaultSingleDestCompletionHandler(CompletableFuture<Boolean> future)
    {
        this.future = future;
    }

    @Override
    public void onAcked()
    {
        future.complete(true);
    }


    @Override
    public void onTimeout()
    {
        future.complete(false);
    }


    @Override
    public void onFailure(Throwable t)
    {
        future.completeExceptionally(t);
    }
}
