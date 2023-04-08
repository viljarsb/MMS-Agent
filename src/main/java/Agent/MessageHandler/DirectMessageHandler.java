package Agent.MessageHandler;

import Agent.MMTP.MessageFormats.DirectApplicationMessage;
import Agent.Utils.Validators.MMTPValidationException;
import Agent.Utils.Validators.MMTPValidator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.Executor;

/**
 * Handles direct application messages.
 */
@Slf4j
public class DirectMessageHandler extends SubjectCastMessageHandler
{
    private final DirectMessageListener iDmMessageListener;


    public DirectMessageHandler(@NonNull DirectMessageListener iDmMessageListener, @NonNull Executor executor)
    {
        super(iDmMessageListener, executor);
        this.iDmMessageListener = iDmMessageListener;
    }


    /**
     * Processes a direct application message and notifies the {@link DirectMessageListener}.
     *
     * @param message the direct application message to process
     */
    @Override
    protected void processDirectApplicationMessage(@NonNull DirectApplicationMessage message)
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
        var destinations = message.getRecipientsList();
        var sender = message.getSender();
        var expires = Instant.ofEpochSecond(message.getExpires().getSeconds(), message.getExpires().getNanos());
        var content = message.getPayload().toByteArray();

        iDmMessageListener.onDirectMessage(messageId, destinations, sender, expires, content);
    }
}
