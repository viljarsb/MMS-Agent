package SMMPClient.Acks.AckEntry;

import SMMPClient.Acks.DeliveryResult;
import SMMPClient.Acks.DeliveryResultSubjectCast;
import SMMPClient.Acks.Listeners.SubjectDeliveryCompletionListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DefaultSubjectCastDestCompletionHandler implements SubjectDeliveryCompletionListener
{
    private final CompletableFuture<DeliveryResultSubjectCast> future;

    public DefaultSubjectCastDestCompletionHandler(CompletableFuture<DeliveryResultSubjectCast> future)
    {
        this.future = future;
    }

    @Override
    public void onAck(String destination)
    {
        //ignore
    }


    @Override
    public void onExpired(List<String> acknowledged)
    {
        DeliveryResultSubjectCast result = new DeliveryResultSubjectCast(acknowledged);
        future.complete(result);
    }


    @Override
    public void onFailure(Throwable t)
    {
        future.completeExceptionally(t);
    }
}
