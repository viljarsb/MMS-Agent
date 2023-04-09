package SMMPClient.Acks.Listeners;

public interface SingleDeliveryCompletionHandler
{
    void onAcked();
    void onTimeout();
    void onFailure(Throwable t);
}
