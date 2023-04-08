package Agent.Connections;

import Agent.Exceptions.ConnectException;
import Agent.MessageSending.MMTPSendingListener;
import Agent.Subscriptions.ISubscribeListener;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IAuthenticatedConnection extends IAnonymousConnection
{
    void subscribeToDM(ISubscribeListener listener) throws ConnectException;

    CompletableFuture<Void> subscribeToDM() throws ConnectException;

    void sendDirect(String destination, byte[] payload, Instant expires, MMTPSendingListener listener) throws ConnectException;

    CompletableFuture<String> sendDirect(String subject, byte[] payload, Instant expires) throws ConnectException;

    void sendDirect(List<String> destinations, byte[] payload, Instant expires, MMTPSendingListener listener) throws ConnectException;

    CompletableFuture<String> sendDirect(List<String> destinations, byte[] payload, Instant expires) throws ConnectException;

    void publish(String subject, byte[] payload, Instant expires, MMTPSendingListener listener) throws ConnectException;

    CompletableFuture<String> publish(String subject, byte[] payload, Instant expires) throws ConnectException;
}