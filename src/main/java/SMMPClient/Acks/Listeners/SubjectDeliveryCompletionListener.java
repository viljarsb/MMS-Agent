package SMMPClient.Acks.Listeners;

import java.util.List;

public interface SubjectDeliveryCompletionListener
{
    void onAck(String destination);
    void onExpired(List<String> acknowledged);
    void onFailure(Throwable t);
}
