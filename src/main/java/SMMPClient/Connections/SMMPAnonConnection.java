package SMMPClient.Connections;

import Agent.Connections.AnonymousConnection;
import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * A wrapper around an AnonymousConnection that provides a more convenient interface for SMMP.
 * This wrapper really doesn't do much, but it does make the API a little more intuitive,
 * and users of this library don't need to know about the Agent library.
 */
public class SMMPAnonConnection
{
    private final AnonymousConnection connection;


    /**
     * Creates a new SMMPAnonConnection.
     *
     * @param connection The connection to wrap.
     */
    public SMMPAnonConnection(AnonymousConnection connection)
    {
        this.connection = connection;
    }


    /**
     * Subscribes the connection to a single subject.
     *
     * @param subject  The subject to subscribe to.
     * @param listener The listener to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    public void subscribeToSubject(@NonNull String subject, @NonNull ISubscribeListener listener) throws ConnectException
    {
        connection.subscribeToSubject(subject, listener);
    }


    /**
     * Subscribes the connection to a single subject.
     *
     * @param subject The subject to subscribe to.
     * @return A CompletableFuture that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> subscribeToSubject(@NonNull String subject) throws ConnectException
    {
        return connection.subscribeToSubject(subject);
    }


    /**
     * Subscribes the connection to multiple subjects.
     *
     * @param subjects The list of subjects to subscribe to.
     * @param listener The listener to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    public void subscribeToSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener) throws ConnectException
    {
        connection.subscribeToSubjects(subjects, listener);
    }


    /**
     * Subscribes the connection to multiple subjects.
     *
     * @param subjects The list of subjects to subscribe to.
     * @return A CompletableFuture that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> subscribeToSubjects(@NonNull List<String> subjects) throws ConnectException
    {
        return connection.subscribeToSubjects(subjects);
    }


    /**
     * Unsubscribes the connection from a single subject with the given listener.
     *
     * @param subject  The subject to unsubscribe from.
     * @param listener The listener to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    public void unsubscribeFromSubject(String subject, ISubscribeListener listener) throws ConnectException
    {
        connection.unsubscribeFromSubject(subject, listener);
    }


    /**
     * Unsubscribes the connection from a single subject.
     *
     * @param subject The subject to unsubscribe from.
     * @return A CompletableFuture that completes when the unsubscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> unsubscribeFromSubject(String subject) throws ConnectException
    {
        return connection.unsubscribeFromSubject(subject);
    }


    /**
     * Unsubscribes the connection from multiple subjects with the given listener.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @param listener The listener to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    public void unsubscribeFromSubjects(List<String> subjects, ISubscribeListener listener) throws ConnectException
    {
        connection.unsubscribeFromSubjects(subjects, listener);
    }


    /**
     * Unsubscribes the connection from multiple subjects.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @return A CompletableFuture that completes when the unsubscriptions are complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> unsubscribeFromSubjects(List<String> subjects) throws ConnectException
    {
        return connection.unsubscribeFromSubjects(subjects);
    }

}
