package Agent.Connections;

import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * An interface representing an anonymous connection to the edge router.
 */
public interface IAnonymousConnection extends IConnection
{
    /**
     * Subscribes to the specified subject with the given listener.
     *
     * @param subject  The subject to subscribe to.
     * @param listener The listener to call when subscribed.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    void subscribeToSubject(String subject, ISubscribeListener listener) throws ConnectException;

    /**
     * Subscribes to the specified subject.
     *
     * @param subject The subject to subscribe to.
     * @return A CompletableFuture that completes when the subscription is successful.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    CompletableFuture<Void> subscribeToSubject(String subject) throws ConnectException;

    /**
     * Subscribes to the specified subjects with the given listener.
     *
     * @param subjects The subjects to subscribe to.
     * @param listener The listener to call when subscribed.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    void subscribeToSubjects(List<String> subjects, ISubscribeListener listener) throws ConnectException;

    /**
     * Subscribes to the specified subjects.
     *
     * @param subjects The subjects to subscribe to.
     * @return A CompletableFuture that completes when the subscriptions are successful.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    CompletableFuture<Void> subscribeToSubjects(List<String> subjects) throws ConnectException;

    /**
     * Unsubscribes from the specified subject with the given listener.
     *
     * @param subject  The subject to unsubscribe from.
     * @param listener The listener to call when unsubscribed.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    void unsubscribeFromSubject(String subject, ISubscribeListener listener) throws ConnectException;

    /**
     * Unsubscribes from the specified subject.
     *
     * @param subject The subject to unsubscribe from.
     * @return A CompletableFuture that completes when the unsubscription is successful.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    CompletableFuture<Void> unsubscribeFromSubject(String subject) throws ConnectException;

    /**
     * Unsubscribes from the specified subjects with the given listener.
     *
     * @param subjects The subjects to unsubscribe from.
     * @param listener The listener to call when unsubscribed.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    void unsubscribeFromSubjects(List<String> subjects, ISubscribeListener listener) throws ConnectException;

    /**
     * Unsubscribes from the specified subjects.
     *
     * @param subjects The subjects to unsubscribe from.
     * @return A CompletableFuture that completes when the unsubscriptions are successful.
     * @throws ConnectException If there is an error connecting to the edge router.
     */
    CompletableFuture<Void> unsubscribeFromSubjects(List<String> subjects) throws ConnectException;
}