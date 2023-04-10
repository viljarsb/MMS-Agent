package SMMPClient.Connections;

import Agent.Connections.IAnonymousConnection;
import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * A wrapper around an {@link Agent.Connections.IAnonymousConnection} that provides a more convenient interface for SMMP.
 * This wrapper really doesn't do much, but it does make the API a little more intuitive,
 * and users of this library don't need to know about the Agent library.
 */
@Slf4j
public class SMMPAnonConnection implements ISMMPAnonConnection
{
    private final IAnonymousConnection connection;


    /**
     * Creates a new {@link SMMPAnonConnection}.
     *
     * @param connection {@link IAnonymousConnection} to wrap.
     */
    public SMMPAnonConnection(@NonNull IAnonymousConnection connection)
    {
        log.debug("SMMPAnonConnection created");
        this.connection = connection;
    }


    /**
     * Subscribes the connection to a single subject.
     *
     * @param subject  The subject to subscribe to.
     * @param listener The {@link ISubscribeListener} to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    public void subscribeToSubject(@NonNull String subject, @NonNull ISubscribeListener listener) throws ConnectException
    {
        log.debug("Subscribing to subject {} with listener {}", subject, listener);
        connection.subscribeToSubject(subject, listener);
        log.debug("Subscription to subject {} with listener {} complete", subject, listener);
    }


    /**
     * Subscribes the connection to a single subject.
     *
     * @param subject The subject to subscribe to.
     * @return A {@link CompletableFuture} that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> subscribeToSubject(@NonNull String subject) throws ConnectException
    {
        log.debug("Subscribing to subject {}", subject);
        CompletableFuture<Void> future = connection.subscribeToSubject(subject);
        log.debug("Subscription to subject {} complete", subject);
        return future;
    }


    /**
     * Subscribes the connection to multiple subjects.
     *
     * @param subjects The list of subjects to subscribe to.
     * @param listener The {@link ISubscribeListener} to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    public void subscribeToSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener) throws ConnectException
    {
        log.debug("Subscribing to subjects {} with listener {}", subjects, listener);
        connection.subscribeToSubjects(subjects, listener);
        log.debug("Subscription to subjects {} with listener {} complete", subjects, listener);
    }


    /**
     * Subscribes the connection to multiple subjects.
     *
     * @param subjects The list of subjects to subscribe to.
     * @return A {@link CompletableFuture} that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> subscribeToSubjects(@NonNull List<String> subjects) throws ConnectException
    {
        log.debug("Subscribing to subjects {}", subjects);
        CompletableFuture<Void> future = connection.subscribeToSubjects(subjects);
        log.debug("Subscription to subjects {} complete", subjects);
        return future;
    }


    /**
     * Unsubscribes the connection from a single subject with the given listener.
     *
     * @param subject  The subject to unsubscribe from.
     * @param listener The {@link ISubscribeListener} to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    public void unsubscribeFromSubject(@NonNull String subject, @NonNull ISubscribeListener listener) throws ConnectException
    {
        log.debug("Unsubscribing from subject {} with listener {}", subject, listener);
        connection.unsubscribeFromSubject(subject, listener);
        log.debug("Unsubscription from subjects {} with listener {} complete", subject, listener);
    }


    /**
     * Unsubscribes the connection from a single subject.
     *
     * @param subject The subject to unsubscribe from.
     * @return A {@link CompletableFuture} that completes when the unsubscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> unsubscribeFromSubject(@NonNull String subject) throws ConnectException
    {
        log.debug("Unsubscribing from subject {}", subject);
        CompletableFuture<Void> future = connection.unsubscribeFromSubject(subject);
        log.debug("Unsubscribed from subject {}", subject);
        return future;
    }


    /**
     * Unsubscribes the connection from multiple subjects with the given listener.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @param listener The {@link ISubscribeListener} to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    public void unsubscribeFromSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener) throws ConnectException
    {
        log.debug("Unsubscribing from subjects {} with listener {}", subjects, listener);
        connection.unsubscribeFromSubjects(subjects, listener);
        log.debug("Unsubscribed from subjects {} with listener {}", subjects, listener);
    }


    /**
     * Unsubscribes the connection from multiple subjects.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @return A {@link CompletableFuture} that completes when the unsubscriptions are complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> unsubscribeFromSubjects(@NonNull List<String> subjects) throws ConnectException
    {
        log.debug("Unsubscribing from subjects {}", subjects);
        CompletableFuture<Void> future = connection.unsubscribeFromSubjects(subjects);
        log.debug("Unsubscribed from subjects {}", subjects);
        return future;
    }
}
