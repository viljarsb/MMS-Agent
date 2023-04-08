package Agent.MessageSending;


import Agent.MMTP.MessageFormats.DirectApplicationMessage;
import Agent.MMTP.MessageFormats.MessageType;
import Agent.MMTP.MessageFormats.SubjectCastApplicationMessage;
import Agent.Utils.ProtocolMessageUtils;
import Agent.Utils.MMTPUtils;
import Agent.Utils.Validators.MMTPValidationException;
import Agent.Utils.Validators.MMTPValidator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;


/**
 * The {@code MMMTPMessageSender} class provides an implementation of the {@code IMMTPMessageSender} interface
 * for sending MMTP (Maritime Messaging Transport Protocol) application messages, including subject-cast and
 * direct messages.
 */
@Slf4j
public class MMTPMessageSender implements IMMTPMessageSender
{
    private final Session session;
    private final String maritimeResourceName;
    private final Executor executor;

    /**
     * Creates a new instance of {@code MMMTPMessageSender} with the given session, maritime resource name, and executor.
     *
     * @param session               the WebSocket session used for sending MMTP messages
     * @param maritimeResourceName  the name of the maritime resource (the MRN) that is sending the messages
     * @param executor              the executor used for asynchronous message sending
     */
    public MMTPMessageSender(@NonNull Session session, @NonNull String maritimeResourceName, @NonNull Executor executor)
    {
        this.session = session;
        this.maritimeResourceName = maritimeResourceName;
        this.executor = executor;
    }

    /**
     * Sends a direct MMTP application message to the given destinations with the given payload and expiration time.
     *
     * @param destinations  the list of destination IDs to send the message to
     * @param payload       the payload of the message
     * @param expires       the time at which the message expires
     * @param sendListener  the listener to invoke when the message has been sent or if sending failed
     */
    @Override
    public void sendDirectMessage(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires, @NonNull MMTPSendingListener sendListener)
    {
        executeAsync(() ->
        {
            try
            {
                DirectApplicationMessage message = prepareDirectMessage(destinations, payload, expires);
                ProtocolMessageUtils.buildAndSendProtocolMessage(session, MessageType.DIRECT_APPLICATION_MESSAGE, message.toByteString());
                log.debug("Sent direct message with ID {} to destinations {}", message.getId(), destinations);
                sendListener.onSuccess(message.getId());
            }
            catch (Exception ex)
            {
                log.error("Failed to send direct message: {}", ex.getMessage());
                sendListener.onFailure(ex);
            }
        });
    }

    /**
     * Sends a direct MMTP application message to the given destinations with the given payload and expiration time,
     * returning a CompletableFuture that completes with the message ID when the message has been sent.
     *
     * @param destinations  the list of destination IDs to send the message to
     * @param payload       the payload of the message
     * @param expires       the time at which the message expires
     * @return              a CompletableFuture that completes with the message ID when the message has been sent
     */
    @Override
    public CompletableFuture<String> sendDirectMessage(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires)
    {
        return executeAsync(() ->
        {
            try
            {
                DirectApplicationMessage message = prepareDirectMessage(destinations, payload, expires);
                ProtocolMessageUtils.buildAndSendProtocolMessage(session, MessageType.DIRECT_APPLICATION_MESSAGE, message.toByteString());
                log.debug("Sent direct message with ID {} to destinations {}", message.getId(), destinations);
                return message.getId();
            }
            catch (Exception ex)
            {
                log.error("Failed to send direct message: {}", ex.getMessage());
                throw new CompletionException(ex);
            }
        });
    }

    /**
     * Publishes a subject-cast application message to the specified subject.
     *
     * @param subject The subject of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @param sendListener The listener for send events.
     */
    @Override
    public void publish(@NonNull String subject, @NonNull byte[] payload, Instant expires, @NonNull MMTPSendingListener sendListener)
    {
        executeAsync(() ->
        {
            try
            {
                SubjectCastApplicationMessage message = prepareSubjectCastMessage(subject, payload, expires);
                ProtocolMessageUtils.buildAndSendProtocolMessage(session, MessageType.SUBJECT_CAST_APPLICATION_MESSAGE, message.toByteString());
                log.debug("Published message with ID {} to subject {}", message.getId(), subject);
                sendListener.onSuccess(message.getId());
            }
            catch (Exception ex)
            {
                log.error("Failed to publish message: {}", ex.getMessage());
                sendListener.onFailure(ex);
            }
        });
    }

    /**
     * Publishes a subject-cast application message to the specified subject.
     *
     * @param subject The subject of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @return A CompletableFuture that completes with the ID of the published message if the message was sent successfully,
     *         or completes exceptionally with a SendingException if the message could not be sent.
     */
    @Override
    public CompletableFuture<String> publish(@NonNull String subject, @NonNull byte[] payload, Instant expires)
    {
        return executeAsync(() ->
        {
            try
            {
                SubjectCastApplicationMessage message = prepareSubjectCastMessage(subject, payload, expires);
                ProtocolMessageUtils.buildAndSendProtocolMessage(session, MessageType.SUBJECT_CAST_APPLICATION_MESSAGE, message.toByteString());
                log.debug("Published message with ID {} to subject {}", message.getId(), subject);
                return message.getId();
            }
            catch (Exception ex)
            {
                log.error("Failed to publish message: {}", ex.getMessage());
                throw new CompletionException(ex);
            }
        });
    }


    /**
     * Executes the specified runnable asynchronously.
     *
     * @param task The task to execute.
     */
    private void executeAsync(Runnable task)
    {
        executor.execute(task);
    }


    /**
     * Executes the specified supplier asynchronously and returns a CompletableFuture containing its result.
     *
     * @param task The task to execute.
     * @param <T> The type of the result.
     * @return A CompletableFuture containing the result of the task.
     */
    private <T> CompletableFuture<T> executeAsync(Supplier<T> task)
    {
        return CompletableFuture.supplyAsync(task, executor);
    }


    /**
     * Prepares a direct application message to the specified destinations with the given payload and expiration time.
     *
     * @param destinations The destinations of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @return The prepared direct application message.
     * @throws Exception if there was an error preparing the message.
     */
    private DirectApplicationMessage prepareDirectMessage(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires) throws Exception
    {
        return createAndValidateDirectApplicationMessage(destinations, payload, expires);
    }


    /**
     * Prepares a subject-cast application message to the specified subject with the given payload and expiration time.
     *
     * @param subject The subject of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @return The prepared subject-cast application message.
     * @throws Exception if there was an error preparing the message.
     */
    private SubjectCastApplicationMessage prepareSubjectCastMessage(@NonNull String subject, @NonNull byte[] payload, Instant expires) throws Exception
    {
        return createAndValidateSubjectCastApplicationMessage(subject, payload, expires);
    }


    /**
     * Creates and validates a direct application message to the specified destinations with the given payload and expiration time.
     *
     * @param destinations The destinations of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @return The created and validated direct application message.
     * @throws MMTPValidationException if the message failed validation.
     */
    private DirectApplicationMessage createAndValidateDirectApplicationMessage(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires) throws MMTPValidationException
    {
        DirectApplicationMessage message = MMTPUtils.createDirectApplicationMessage(destinations, maritimeResourceName, payload, expires);
        MMTPValidator.validate(message);
        return message;
    }


    /**
     * Creates and validates a subject-cast application message to the specified subject with the given payload and expiration time.
     *
     * @param subject The subject of the message.
     * @param payload The payload of the message.
     * @param expires The expiration time of the message.
     * @return The created and validated subject-cast application message.
     * @throws MMTPValidationException if the message failed validation.
     */
    private SubjectCastApplicationMessage createAndValidateSubjectCastApplicationMessage(@NonNull String subject, @NonNull byte[] payload, Instant expires) throws MMTPValidationException
    {
        SubjectCastApplicationMessage message = MMTPUtils.createSubjectCastApplicationMessage(subject, maritimeResourceName, payload, expires);
        MMTPValidator.validate(message);
        return message;
    }
}