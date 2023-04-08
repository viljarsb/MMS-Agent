package Agent.MessageHandler;


import Agent.MMTP.MessageFormats.DirectApplicationMessage;
import Agent.MMTP.MessageFormats.ProtocolMessage;
import Agent.MMTP.MessageFormats.SubjectCastApplicationMessage;
import Agent.Utils.Validators.MMTPValidationException;
import Agent.Utils.Validators.MMTPValidator;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;


import java.time.Instant;
import java.util.concurrent.Executor;

/**
 * Handles subject-cast application messages.
 */
@Slf4j
public class SubjectCastMessageHandler implements MessageHandler
{
    private final SubjectMessageListener messageListener;
    private final Executor executor;


    /**
     * Creates a new instance of the {@link SubjectCastMessageHandler} class.
     *
     * @param messageListener the message listener to handle subject-cast messages
     * @param executor        the executor to execute tasks asynchronously
     */
    public SubjectCastMessageHandler(@NonNull SubjectMessageListener messageListener, @NonNull Executor executor)
    {
        this.messageListener = messageListener;
        this.executor = executor;
    }


    /**
     * Handles an incoming WebSocket message.
     *
     * @param payload the payload of the message
     * @param offset  the offset within the payload where the message content starts
     * @param len     the length of the message content in bytes
     */
    public void handleMessage(@NonNull byte[] payload, int offset, int len)
    {
        executor.execute(() -> processMessageInternal(payload, offset, len));
    }


    /**
     * Processes the message payload and determines the message type, then calls the appropriate handler method.
     *
     * @param payload the payload of the message
     * @param offset  the offset within the payload where the message content starts
     * @param len     the length of the message content in bytes
     */
    private void processMessageInternal(@NonNull byte[] payload, int offset, int len)
    {
        byte[] message = new byte[len];
        System.arraycopy(payload, offset, message, 0, len);

        try
        {
            var protocolMessage = ProtocolMessage.parseFrom(message);
            var type = protocolMessage.getType();

            switch (type)
            {
                case DIRECT_APPLICATION_MESSAGE ->
                {
                    var directApplicationMessage = DirectApplicationMessage.parseFrom(protocolMessage.getContent());
                    processDirectApplicationMessage(directApplicationMessage);
                }

                case SUBJECT_CAST_APPLICATION_MESSAGE ->
                {
                    var subjectCastApplicationMessage = SubjectCastApplicationMessage.parseFrom(protocolMessage.getContent());
                    processSubjectCastApplicationMessage(subjectCastApplicationMessage);
                }

                default -> log.error("Unknown message type: {}", type);
            }
        }
        catch (InvalidProtocolBufferException ex)
        {
            log.error("Invalid protocol buffer", ex);
        }
    }


    /**
     * Processes a direct application message, which is not supported for anonymous agents.
     * Authenticated agents should use the {@link DirectMessageHandler} instead,
     * that supports direct application messages by overriding this method.
     *
     * @param message the direct application message to process
     */
    protected void processDirectApplicationMessage(@NonNull DirectApplicationMessage message)
    {
        log.error("Direct application message is not supported for anonymous agent");
    }


    /**
     * Processes a subject-cast application message.
     *
     * @param message the subject-cast application message to process
     */
    private void processSubjectCastApplicationMessage(@NonNull SubjectCastApplicationMessage message)
    {
        try
        {
            MMTPValidator.validate(message);
        }
        catch (MMTPValidationException ex)
        {
            log.error("The application message is not valid, dropping it", ex);
            return;
        }

        var messageId = message.getId();
        var sender = message.getSender();
        var expires = Instant.ofEpochSecond(message.getExpires().getSeconds(), message.getExpires().getNanos());
        var subject = message.getSubject();
        var content = message.getPayload().toByteArray();

        messageListener.onSubjectCastMessage(messageId, sender, subject, expires, content);
    }
}


