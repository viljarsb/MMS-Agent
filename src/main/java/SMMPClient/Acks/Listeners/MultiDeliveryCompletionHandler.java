package SMMPClient.Acks.Listeners;

import java.util.List;

public interface MultiDeliveryCompletionHandler
{
    void onAck(String destination);
    void onFullyAcked(List<String> destinations);
    void onTimeout(List<String> unacknowledged, List<String> acknowledged);
    void onFailure(Throwable t);
}
