package Agent.Connections;

import Agent.Exceptions.ConnectException;
import Agent.MessageSending.IMMTPMessageSender;
import Agent.MessageSending.MessageSendListener;
import Agent.Subscriptions.IDmSubscriptionHandler;
import Agent.Subscriptions.ISubscribeListener;
import Agent.WebSocket.DisconnectionHook;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.maritimeconnectivity.pki.PKIIdentity;
import org.eclipse.jetty.websocket.api.Session;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * An implementation of the {@link IAuthenticatedConnection} interface representing an authenticated connection to the edge router.
 * This connection is authenticated using an MCP-PKI identity, and has all functionality of MMS.
 */
@Slf4j
public class AuthenticatedConnection extends AnonymousConnection implements IAuthenticatedConnection
{
    private final IDmSubscriptionHandler subscriptionHandler;
    private final IMMTPMessageSender messageSender;
    private final PKIIdentity identity;


    /**
     * Creates a new AuthenticatedConnection instance.
     *
     * @param session             The underlying WebSocket session.
     * @param disconnectionHook   A hook to be called when the connection is disconnected.
     * @param subscriptionHandler The subject subscription handler.
     * @param messageSender       The MTTP message sender.
     * @param identity            The PKI identity of the user.
     */
    public AuthenticatedConnection(Session session, DisconnectionHook disconnectionHook, IDmSubscriptionHandler subscriptionHandler, IMMTPMessageSender messageSender, PKIIdentity identity)
    {
        super(session, disconnectionHook, subscriptionHandler);
        this.connectionStatus = ConnectionStatus.CONNECTED_AUTHENTICATED;
        this.subscriptionHandler = subscriptionHandler;
        this.messageSender = messageSender;
        this.identity = identity;
    }


    /**
     * Returns whether the connection is authenticated.
     *
     * @return True if the connection is authenticated, false otherwise.
     */
    @Override
    public boolean isAuthenticated()
    {
        return true;
    }


    /**
     * Subscribes to direct messages with a listener.
     *
     * @param listener The subscribe listener.
     * @throws ConnectException If the connection is not alive.
     */
    public void subscribeToDM(@NonNull ISubscribeListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Subscribing to direct messages.");
        subscriptionHandler.subscribeToDM(listener);
    }


    /**
     * Subscribes to direct messages asynchronously.
     *
     * @return A completable future that completes when the subscription is complete.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<Void> subscribeToDM() throws ConnectException
    {
        checkConnectionAlive();
        log.info("Subscribing to direct messages.");
        return subscriptionHandler.subscribeToDM();
    }


    /**
     * Sends a direct message to a destination with a listener.
     *
     * @param destination The destination of the message.
     * @param payload     The payload of the message.
     * @param expires     The expiration time of the message.
     * @param listener    The message send listener.
     * @throws ConnectException If the connection is not alive.
     */
    public void sendDirect(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull MessageSendListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Sending direct message to destination: {}", destination);
        sendDirect(List.of(destination), payload, expires, listener);
    }


    /**
     * Sends a direct message to a subject asynchronously.
     *
     * @param subject The subject of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @return A completable future that completes when the message is sent.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<String> sendDirect(@NonNull String subject, byte[] payload, Instant expires) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Sending direct message to subject: {}", subject);
        return sendDirect(List.of(subject), payload, expires);
    }


    /**
     * Sends a direct message to a list of destinations with a listener.
     *
     * @param destinations The destinations of the message.
     * @param payload      The payload of the message.
     * @param expires      The expiration time of the message.
     * @param listener     The message send listener.
     * @throws ConnectException If the connection is not alive.
     */
    public void sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires, @NonNull MessageSendListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Sending direct message to destinations: {}", destinations);
        messageSender.sendDirectMessage(destinations, payload, expires, listener);
    }


    /**
     * Sends a direct message to a list of destinations asynchronously.
     *
     * @param destinations The destinations of the message.
     * @param payload      The payload of the message.
     * @param expires      The expiration time of the message.
     * @return A completable future that completes when the message is sent.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<String> sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Sending direct message to destinations: {}", destinations);
        return messageSender.sendDirectMessage(destinations, payload, expires);
    }


    /**
     * Publishes a message to a subject with a listener.
     *
     * @param subject  The subject of the message.
     * @param payload  The payload of the message.
     * @param expires  The expiration time of the message.
     * @param listener The message send listener.
     * @throws ConnectException If the connection is not alive.
     */
    public void publish(@NonNull String subject, @NonNull byte[] payload, Instant expires, @NonNull MessageSendListener listener) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Publishing message to subject: {}", subject);
        messageSender.publish(subject, payload, expires, listener);
    }


    /**
     * Publishes a message to a subject asynchronously.
     *
     * @param subject The subject of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @return A completable future that completes when the message is sent.
     * @throws ConnectException If the connection is not alive.
     */
    public CompletableFuture<String> publish(@NonNull String subject, @NonNull byte[] payload, Instant expires) throws ConnectException
    {
        checkConnectionAlive();
        log.info("Publishing message to subject: {}", subject);
        return messageSender.publish(subject, payload, expires);
    }


    @Override
    public PKIIdentity getIdentity()
    {
        return identity;
    }
}
