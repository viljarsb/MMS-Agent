package SMMPClient.MessageFormats;

import com.google.protobuf.ByteString;

public class SMMPUtils
{
    public static SMMPMessage createMessage(byte[] payload, byte[] signature, String messageID, byte[] certificate, boolean isEncrypted, boolean requiresAck)
    {
        return SMMPMessage.newBuilder()
                .setMessageID(messageID)
                .setRequiresAck(requiresAck)
                .setIsEncrypted(isEncrypted)
                .setPayload(ByteString.copyFrom(payload))
                .setSignature(ByteString.copyFrom(signature))
                .setCertificate(ByteString.copyFrom(certificate))
                .build();
    }


}
