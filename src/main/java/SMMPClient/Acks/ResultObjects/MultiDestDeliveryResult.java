package SMMPClient.Acks.ResultObjects;

import java.util.Collections;
import java.util.List;

/**
 * Represents the delivery result for multiple destination acknowledgements.
 */
public class MultiDestDeliveryResult
{
    private final List<String> ackedDestinations;
    private final List<String> failedDestinations;


    /**
     * Constructs a new {@link MultiDestDeliveryResult} instance.
     *
     * @param ackedDestinations  the list of destinations that acknowledged the message
     * @param failedDestinations the list of destinations that failed to acknowledge the message
     */
    public MultiDestDeliveryResult(List<String> ackedDestinations, List<String> failedDestinations)
    {
        this.ackedDestinations = ackedDestinations;
        this.failedDestinations = failedDestinations;
    }


    /**
     * Returns the list of destinations that acknowledged the message.
     *
     * @return an unmodifiable list of acknowledged destinations
     */
    public List<String> getAckedDestinations()
    {
        return Collections.unmodifiableList(ackedDestinations);
    }


    /**
     * Returns the list of destinations that failed to acknowledge the message.
     *
     * @return an unmodifiable list of failed destinations
     */
    public List<String> getFailedDestinations()
    {
        return Collections.unmodifiableList(failedDestinations);
    }
}