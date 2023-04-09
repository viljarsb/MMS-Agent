package SMMPClient.Acks;

import java.util.Collections;
import java.util.List;

public class DeliveryResultSubjectCast
{
    private final List<String> ackedDestinations;


    public DeliveryResultSubjectCast(List<String> ackedDestinations)
    {
        this.ackedDestinations = ackedDestinations;
    }


    public List<String> getAckedDestinations()
    {
        return Collections.unmodifiableList(ackedDestinations);
    }
}