package Agent.MessageHandler;

import java.util.concurrent.Executor;

/**
 * Factory class for creating {@link MessageHandler} instances.
 */
public class MessageHandlerFactory
{
    /**
     * Creates a new {@link MessageHandler} instance.
     *
     * @param messageListener the message listener to handle application messages
     * @param executor        the executor to execute tasks asynchronously
     * @return a new {@link MessageHandler} instance
     */
    public static MessageHandler createMessageHandler(SubjectMessageListener messageListener, Executor executor)
    {
        if (messageListener instanceof DirectMessageListener)
        {
            return new DirectMessageHandler((DirectMessageListener) messageListener, executor);
        }
        else
        {
            return new SubjectCastMessageHandler(messageListener, executor);
        }
    }
}