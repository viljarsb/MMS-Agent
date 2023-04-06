package Agent.Subscriptions;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Defines an interface for managing subscriptions to subjects.
 */
public interface ISubjectSubscriptionHandler
{
    /**
     * Subscribes to the given list of subjects and notifies the given listener when the operation is complete.
     *
     * @param subjects the subjects to subscribe to
     * @param listener the listener to notify when the operation is complete
     */
    void subscribeToSubjects(List<String> subjects, ISubscribeListener listener);

    /**
     * Subscribes to the given list of subjects and returns a {@link CompletableFuture} that completes when the
     * operation is complete.
     *
     * @param subjects the subjects to subscribe to
     * @return a CompletableFuture that completes when the operation is complete
     */
    CompletableFuture<Void> subscribeToSubjects(List<String> subjects);

    /**
     * Unsubscribes from the given list of subjects and notifies the given listener when the operation is complete.
     *
     * @param subjects the subjects to unsubscribe from
     * @param listener the listener to notify when the operation is complete
     */
    void unsubscribeFromSubjects(List<String> subjects, ISubscribeListener listener);

    /**
     * Unsubscribes from the given list of subjects and returns a {@link CompletableFuture} that completes when the
     * operation is complete.
     *
     * @param subjects the subjects to unsubscribe from
     * @return a CompletableFuture that completes when the operation is complete
     */
    CompletableFuture<Void> unsubscribeFromSubjects(List<String> subjects);

    /**
     * Returns a copy of the current subscriptions.
     *
     * @return a list of the current subscriptions
     */
    List<String> getSubscriptions();

    /**
     * Checks whether the given subject is currently subscribed.
     *
     * @param subject the subject to check
     * @return true if the subject is subscribed, false otherwise
     */
    boolean isSubscribed(String subject);

    /**
     * Clears all current subscriptions.
     */
    void clear();
}