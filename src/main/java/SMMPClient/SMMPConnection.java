package SMMPClient;

import Agent.Connections.AuthenticatedConnection;
import Agent.Exceptions.ConnectException;
import Agent.MessageSending.IMMTPMessageSender;
import Agent.MessageSending.MessageSendListener;
import Agent.Subscriptions.IDmSubscriptionHandler;
import Agent.WebSocket.DisconnectionHook;
import lombok.NonNull;
import net.maritimeconnectivity.pki.PKIIdentity;
import org.eclipse.jetty.websocket.api.Session;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SMMPConnection extends AuthenticatedConnection
{
    /**
     * Creates a new AuthenticatedConnection instance.
     *
     * @param session             The underlying WebSocket session.
     * @param disconnectionHook   A hook to be called when the connection is disconnected.
     * @param subscriptionHandler The subject subscription handler.
     * @param messageSender       The MTTP message sender.
     * @param identity            The PKI identity of the user.
     */
    public SMMPConnection(Session session, DisconnectionHook disconnectionHook, IDmSubscriptionHandler subscriptionHandler, IMMTPMessageSender messageSender, PKIIdentity identity)
    {
        super(session, disconnectionHook, subscriptionHandler, messageSender, identity);
    }

    @Override
    public void sendDirect(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull MessageSendListener listener) throws ConnectException
    {
    }


    public CompletableFuture<String> sendDirect(@NonNull String subject, byte[] payload, Instant expires) throws ConnectException
    {
        return null;
    }


    public void sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires, @NonNull MessageSendListener listener) throws ConnectException
    {
    }


    public CompletableFuture<String> sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires) throws ConnectException
    {
        return null;
    }


    public void publish(@NonNull String subject, @NonNull byte[] payload, Instant expires, @NonNull MessageSendListener listener) throws ConnectException
    {
    }


    public CompletableFuture<String> publish(@NonNull String subject, @NonNull byte[] payload, Instant expires) throws ConnectException
    {
        return null;
    }
}
