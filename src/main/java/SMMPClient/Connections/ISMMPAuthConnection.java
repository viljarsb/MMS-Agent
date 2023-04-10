package SMMPClient.Connections;

import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;
import SMMPClient.Acks.Handlers.MultiDeliveryCompletionHandler;
import SMMPClient.Acks.Handlers.SMMPSendingHandler;
import SMMPClient.Acks.Handlers.SingleDeliveryCompletionHandler;
import SMMPClient.Acks.ResultObjects.MultiDestDeliveryResult;
import SMMPClient.Acks.ResultObjects.SingleDestDeliveryResult;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ISMMPAuthConnection
{
    void sendDirectWithAcknowledgement(String destination, byte[] payload, Instant expires, Boolean encrypt, SingleDeliveryCompletionHandler handler) throws ConnectException;

    CompletableFuture<SingleDestDeliveryResult> sendDirectWithAcknowledgement(String destination, byte[] payload, Instant expires, Boolean encrypt) throws ConnectException;

    CompletableFuture<Boolean> sendDirect(String destination, byte[] payload, Instant expires, Boolean encrypt) throws ConnectException;

    void sendDirect(String destination, byte[] payload, Instant expires, Boolean encrypt, SMMPSendingHandler listener) throws ConnectException;

    void sendDirectWithAcknowledgement(List<String> destinations, byte[] payload, Instant expires, MultiDeliveryCompletionHandler handler) throws ConnectException;

    CompletableFuture<MultiDestDeliveryResult> sendDirectWithAcknowledgement(List<String> destinations, byte[] payload, Instant expires) throws ConnectException;

    CompletableFuture<Boolean> sendDirect(List<String> destinations, byte[] payload, Instant expires) throws ConnectException;

    void sendDirect(List<String> destinations, byte[] payload, Instant expires, Boolean encrypt, SMMPSendingHandler listener) throws ConnectException;

    void publish(String subject, byte[] payload, Instant expires, SMMPSendingHandler listener) throws ConnectException;

    CompletableFuture<Boolean> publish(String subject, byte[] payload, Instant expires) throws ConnectException;

    void subscribeToSubject(String subject, ISubscribeListener listener) throws ConnectException;

    CompletableFuture<Void> subscribeToSubject(String subject) throws ConnectException;

    void subscribeToSubjects(List<String> subjects, ISubscribeListener listener) throws ConnectException;

    CompletableFuture<Void> subscribeToSubjects(List<String> subjects) throws ConnectException;

    void unsubscribeFromSubject(String subject, ISubscribeListener listener) throws ConnectException;

    CompletableFuture<Void> unsubscribeFromSubject(String subject) throws ConnectException;

    void unsubscribeFromSubjects(List<String> subjects, ISubscribeListener listener) throws ConnectException;

    CompletableFuture<Void> unsubscribeFromSubjects(List<String> subjects) throws ConnectException;
}
