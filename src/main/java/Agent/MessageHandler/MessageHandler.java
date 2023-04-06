package Agent.MessageHandler;

/**
 * Interface for handling WebSocket messages.
 */
public interface MessageHandler
{
    /**
     * Called when a WebSocket message is received.
     *
     * @param payload the payload of the message
     * @param offset  the offset within the payload where the message content starts
     * @param len     the length of the message content in bytes
     */
    void handleMessage(byte[] payload, int offset, int len);
}
