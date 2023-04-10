package SMMPClient.Acks.Handlers;

import java.util.List;

public interface SubjectDeliveryCompletionHandler
{
    void onAck(String destination);

    void onExpired(List<String> acknowledged);

    void onFailure(Throwable t);
}
