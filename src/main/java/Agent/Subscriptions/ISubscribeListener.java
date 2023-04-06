package Agent.Subscriptions;

/**
 * A listener interface for subscription operations.
 * <p>
 * Implementations of this interface can be used to receive notifications when a subscription operation completes, either
 * successfully or with an exception.
 */
public interface ISubscribeListener {
    /**
     * Called when a subscription operation completes successfully.
     */
    void onSuccess();

    /**
     * Called when a subscription operation completes with an exception.
     *
     * @param ex the throwable that occurred
     */
    void onFailure(Throwable ex);
}