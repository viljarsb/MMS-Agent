package Agent.MessageSending;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This interface represents a message sender for MMTP (Maritime Messaging Transfer Protocol)
 * application messages, which include subject-cast and directed messages.
 */
public interface IMMTPMessageSender
{
    /**
     * Sends a directed message to the specified destinations with the given payload and expiration time.
     *
     * @param destinations a list of destination MMSIs
     * @param payload the message payload
     * @param expires the expiration time
     * @param sendListener a listener to be notified of the send result
     */
    void sendDirectMessage(List<String> destinations, byte[] payload, Instant expires, MessageSendListener sendListener);

    /**
     * Sends a directed message to the specified destinations with the given payload and expiration time.
     * Returns a {@code CompletableFuture} that completes with the message ID on success or with an
     * exception on failure.
     *
     * @param destinations a list of destination MMSIs
     * @param payload the message payload
     * @param expires the expiration time
     * @return a {@code CompletableFuture} that completes with the message ID on success or with an
     * exception on failure
     */
    CompletableFuture<String> sendDirectMessage(List<String> destinations, byte[] payload, Instant expires);

    /**
     * Publishes a subject-cast message with the given subject, payload, and expiration time.
     *
     * @param subject the message subject
     * @param payload the message payload
     * @param expires the expiration time
     * @param sendListener a listener to be notified of the send result
     */
    void publish(String subject, byte[] payload, Instant expires, MessageSendListener sendListener);
    /**
     * Publishes a subject-cast message with the given subject, payload, and expiration time.
     * Returns a {@code CompletableFuture} that completes with the message ID on success or with an
     * exception on failure.
     *
     * @param subject the message subject
     * @param payload the message payload
     * @param expires the expiration time
     * @return a {@code CompletableFuture} that completes with the message ID on success or with an
     * exception on failure
     */
    CompletableFuture<String> publish(String subject, byte[] payload, Instant expires);
}