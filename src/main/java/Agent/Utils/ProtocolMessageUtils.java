package Agent.Utils;

import Protocols.MMTP.MessageFormats.MessageType;
import Protocols.MMTP.MessageFormats.ProtocolMessage;
import com.google.protobuf.ByteString;
import lombok.NonNull;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ProtocolMessageUtils
{
    private ProtocolMessageUtils() { }


    /**
     * Builds a {@link ProtocolMessage} with the given message type and content.
     *
     * @param messageType the type of message to build
     * @param content     the content of the message
     * @return a ProtocolMessage with the given message type and content
     */
    private static ProtocolMessage buildProtocolMessage(@NonNull MessageType messageType, @NonNull ByteString content)
    {
        return ProtocolMessage.newBuilder().setType(messageType).setContent(content).build();
    }


    /**
     * Converts the given {@link ProtocolMessage} to a byte buffer and sends it to the given session.
     * @param session the session to send the message to
     * @param message the message to send
     * @throws IOException if an I/O error occurs
     */
    private static void sendProtocolMessage(@NonNull Session session, @NonNull ProtocolMessage message) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.wrap(message.toByteArray());
        session.getRemote().sendBytes(buffer);
    }


    /**
     * Builds a {@link ProtocolMessage} with the given message type and content and sends it to the given session.
     *
     * @param session the session to send the message to
     * @param messageType the type of message to build
     * @param content the content of the message
     * @throws IOException if an I/O error occurs
     */
    public static void buildAndSendProtocolMessage(@NonNull Session session, @NonNull MessageType messageType, @NonNull ByteString content) throws IOException
    {
        sendProtocolMessage(session, buildProtocolMessage(messageType, content));
    }
}
