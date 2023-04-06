package Agent.Connections;

import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;
import Agent.Subscriptions.ISubjectSubscriptionHandler;
import Agent.WebSocket.DisconnectionHook;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * An implementation of the {@link IAnonymousConnection} interface representing an anonymous connection to the edge router.
 */
@Slf4j
public class AnonymousConnection extends Connection implements IAnonymousConnection
{
    protected ISubjectSubscriptionHandler subscriptionHandler;


    /**
     * Creates a new AnonymousConnection instance.
     *
     * @param session             The underlying WebSocket session.
     * @param disconnectionHook   A hook to be called when the connection is disconnected.
     * @param subscriptionHandler The handler for subject subscriptions.
     */
    public AnonymousConnection(Session session, DisconnectionHook disconnectionHook, ISubjectSubscriptionHandler subscriptionHandler)
    {
        super(session, disconnectionHook);
        this.connectionStatus = ConnectionStatus.CONNECTED_ANONYMOUS;
        this.subscriptionHandler = subscriptionHandler;
    }


    /**
     * Returns whether the connection is authenticated.
     *
     * @return Always returns false for anonymous connections.
     */
    @Override
    public boolean isAuthenticated()
    {
        return false;
    }


    /**
     * Subscribes the connection to a single subject.
     *
     * @param subject  The subject to subscribe to.
     * @param listener The listener to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public void subscribeToSubject(@NonNull String subject, @NonNull ISubscribeListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Subscribing to subject: {}", subject);
        subscribeToSubjects(List.of(subject), listener);
    }


    /**
     * Subscribes the connection to a single subject.
     *
     * @param subject The subject to subscribe to.
     * @return A CompletableFuture that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public CompletableFuture<Void> subscribeToSubject(@NonNull String subject) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Subscribing to subject: {}", subject);
        return subscribeToSubjects(List.of(subject));
    }


    /**
     * Subscribes the connection to multiple subjects.
     *
     * @param subjects The list of subjects to subscribe to.
     * @param listener The listener to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public void subscribeToSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Subscribing to subjects: {}", subjects);
        subscriptionHandler.subscribeToSubjects(subjects, listener);
    }


    /**
     * Subscribes the connection to multiple subjects.
     *
     * @param subjects The list of subjects to subscribe to.
     * @return A CompletableFuture that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public CompletableFuture<Void> subscribeToSubjects(@NonNull List<String> subjects) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Subscribing to subjects: {}", subjects);
        return subscriptionHandler.subscribeToSubjects(subjects);
    }


    /**
     * Unsubscribes the connection from a single subject with the given listener.
     *
     * @param subject  The subject to unsubscribe from.
     * @param listener The listener to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public void unsubscribeFromSubject(String subject, ISubscribeListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Unsubscribing from subject: {}", subject);
        unsubscribeFromSubjects(List.of(subject), listener);
    }


    /**
     * Unsubscribes the connection from a single subject.
     *
     * @param subject The subject to unsubscribe from.
     * @return A CompletableFuture that completes when the unsubscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public CompletableFuture<Void> unsubscribeFromSubject(String subject) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Unsubscribing from subject: {}", subject);
        return unsubscribeFromSubjects(List.of(subject));
    }


    /**
     * Unsubscribes the connection from multiple subjects with the given listener.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @param listener The listener to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public void unsubscribeFromSubjects(List<String> subjects, ISubscribeListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Unsubscribing from subjects: {}", subjects);
        subscriptionHandler.unsubscribeFromSubjects(subjects, listener);
    }


    /**
     * Unsubscribes the connection from multiple subjects.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @return A CompletableFuture that completes when the unsubscriptions are complete.
     * @throws ConnectException If the connection is not alive.
     */
    @Override
    public CompletableFuture<Void> unsubscribeFromSubjects(List<String> subjects) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Unsubscribing from subjects: {}", subjects);
        return subscriptionHandler.unsubscribeFromSubjects(subjects);
    }
}
