package SMMPClient.MessageFormats;

import com.google.protobuf.ByteString;

public class SMMPUtils
{
    public static SMMPMessage createMessage(byte[] payload, byte[] signature, String messageID, byte[] certificate, boolean encrypted, boolean requireAck)
    {
        return SMMPMessage.newBuilder().setMessageID(messageID).setSignature(ByteString.copyFrom(signature)).setPayload(ByteString.copyFrom(payload)).setCertificate(ByteString.copyFrom(certificate)).setIsEncrypted(encrypted).setRequiresAck(requireAck).build();
    }


    public static SMMPAck createAck(String messageID, byte[] signature, byte[] certificate)
    {
        return SMMPAck.newBuilder().setMessageID(messageID).setSignature(ByteString.copyFrom(signature)).setCertificate(ByteString.copyFrom(certificate)).build();
    }


    public static ProtocolMessage createProtocolMessage(byte[] message, MessageType messageType)
    {
        return ProtocolMessage.newBuilder().setType(messageType).setContent(ByteString.copyFrom(message)).build();
    }
}

