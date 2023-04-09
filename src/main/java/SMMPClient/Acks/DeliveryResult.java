package SMMPClient.Acks;

import java.util.Collections;
import java.util.List;

public class DeliveryResult
{
    private final List<String> ackedDestinations;
    private final List<String> failedDestinations;


    public DeliveryResult(List<String> ackedDestinations, List<String> failedDestinations)
    {
        this.ackedDestinations = ackedDestinations;
        this.failedDestinations = failedDestinations;
    }


    public List<String> getAckedDestinations()
    {
        return Collections.unmodifiableList(ackedDestinations);
    }


    public List<String> getFailedDestinations()
    {
        return Collections.unmodifiableList(failedDestinations);
    }
}