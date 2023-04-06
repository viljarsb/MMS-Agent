package Agent.Subscriptions;


import java.util.concurrent.CompletableFuture;

/**
 * Defines an interface for managing subscriptions to direct messages (DMs).
 * <p>
 * This interface extends {@link ISubjectSubscriptionHandler} and adds methods for subscribing to and unsubscribing from DMs.
 */
public interface IDmSubscriptionHandler extends ISubjectSubscriptionHandler
{
    /**
     * Subscribes to direct messages and notifies the given listener when the operation is complete.
     *
     * @param subscribeListener the listener to notify when the operation is complete
     */
    void subscribeToDM(ISubscribeListener subscribeListener);

    /**
     * Subscribes to direct messages and returns a {@link CompletableFuture} that completes when the operation is complete.
     *
     * @return a CompletableFuture that completes when the operation is complete
     */
    CompletableFuture<Void> subscribeToDM();

    /**
     * Unsubscribes from direct messages and notifies the given listener when the operation is complete.
     *
     * @param unsubscribeListener the listener to notify when the operation is complete
     */
    void unsubscribeFromDM(ISubscribeListener unsubscribeListener);

    /**
     * Unsubscribes from direct messages and returns a {@link CompletableFuture} that completes when the operation is complete.
     *
     * @return a CompletableFuture that completes when the operation is complete
     */
    CompletableFuture<Void> unsubscribeFromDM();

    /**
     * Returns true if this subscription handler is subscribed to direct messages, false otherwise.
     *
     * @return true if this subscription handler is subscribed to direct messages, false otherwise
     */
    boolean isSubscribedToDM();
}
