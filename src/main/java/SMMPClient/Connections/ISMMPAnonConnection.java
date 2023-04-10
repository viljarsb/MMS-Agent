package SMMPClient.Connections;

import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The {@code ISMMPAnonConnection} interface represents a connection to an SMMP edge router with anonymous access.
 * It provides methods for subscribing and unsubscribing to subjects.
 */
public interface ISMMPAnonConnection
{
    /**
     * Subscribes the connection to a single subject with the given {@link ISubscribeListener}.
     *
     * @param subject  The subject to subscribe to.
     * @param listener The {@link ISubscribeListener} to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    void subscribeToSubject(@NonNull String subject, @NonNull ISubscribeListener listener) throws ConnectException;

    /**
     * Subscribes the connection to a single subject.
     *
     * @param subject The subject to subscribe to.
     * @return A {@link CompletableFuture} that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    CompletableFuture<Void> subscribeToSubject(@NonNull String subject) throws ConnectException;

    /**
     * Subscribes the connection to multiple subjects with the given {@link ISubscribeListener}.
     *
     * @param subjects The list of subjects to subscribe to.
     * @param listener The {@link ISubscribeListener} to be notified of subscription events.
     * @throws ConnectException If the connection is not alive.
     */
    void subscribeToSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener) throws ConnectException;

    /**
     * Subscribes the connection to multiple subjects.
     *
     * @param subjects The list of subjects to subscribe to.
     * @return A {@link CompletableFuture} that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    CompletableFuture<Void> subscribeToSubjects(@NonNull List<String> subjects) throws ConnectException;

    /**
     * Unsubscribes the connection from a single subject with the given {@link ISubscribeListener}.
     *
     * @param subject  The subject to unsubscribe from.
     * @param listener The {@link ISubscribeListener} to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    void unsubscribeFromSubject(@NonNull String subject, @NonNull ISubscribeListener listener) throws ConnectException;

    /**
     * Unsubscribes the connection from a single subject.
     *
     * @param subject The subject to unsubscribe from.
     * @return A {@link CompletableFuture} that completes when the unsubscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    CompletableFuture<Void> unsubscribeFromSubject(@NonNull String subject) throws ConnectException;

    /**
     * Unsubscribes the connection from multiple subjects with the given {@link ISubscribeListener}.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @param listener The {@link ISubscribeListener} to be notified of when unsubscribed.
     * @throws ConnectException If the connection is not alive.
     */
    void unsubscribeFromSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener) throws ConnectException;

    /**
     * Unsubscribes the connection from multiple subjects.
     *
     * @param subjects The list of subjects to unsubscribe from.
     * @return A {@link CompletableFuture} that completes when the unsubscriptions are complete.
     * @throws ConnectException If the connection is not alive.
     */
    CompletableFuture<Void> unsubscribeFromSubjects(@NonNull List<String> subjects) throws ConnectException;
}