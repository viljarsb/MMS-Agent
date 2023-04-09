package SMMPClient.Connections;


import Agent.Connections.AuthenticatedConnection;
import Agent.Exceptions.ConnectException;
import SMMPClient.Acks.*;
import SMMPClient.Acks.Listeners.MultiDeliveryCompletionHandler;
import SMMPClient.Acks.Listeners.SMMPSendingListener;
import SMMPClient.Acks.Listeners.SingleDeliveryCompletionHandler;
import SMMPClient.Crypto.CryptoUtils;
import SMMPClient.Crypto.KeyringManager;
import SMMPClient.Exceptions.CryptoExceptions.EncryptionException;
import SMMPClient.Exceptions.CryptoExceptions.SignatureGenerationException;
import SMMPClient.Exceptions.PkiExceptions.CertificateValidationException;
import SMMPClient.Exceptions.PkiExceptions.MissingCertificateException;
import SMMPClient.MessageFormats.MessageType;
import SMMPClient.MessageFormats.ProtocolMessage;
import SMMPClient.MessageFormats.SMMPMessage;
import SMMPClient.MessageFormats.SMMPUtils;
import SMMPClient.MessageFormats.SMMPAckResponse;
import com.google.protobuf.ByteString;
import lombok.NonNull;

import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * A wrapper around an authenticated connection that provides SMMP functionality.
 * Provides a way to send SMMP messages to other entities.
 * <p>
 * ALl messages are signed, and direct single recipient messages are encrypted (optional).
 */
public class SMMPAuthConnection extends SMMPAnonConnection implements ResendHook
{
    private final AuthenticatedConnection connection;
    private final KeyringManager keyringManager;
    private final AckTracker ackTracker;


    /**
     * Creates a new SMMPAuthConnection.
     *
     * @param connection     The authenticated connection to wrap.
     * @param keyringManager The keyring manager to use for signing and encryption.
     * @param ackTracker     The ack tracker to use for tracking acks.
     */
    public SMMPAuthConnection(AuthenticatedConnection connection, KeyringManager keyringManager, AckTracker ackTracker)
    {
        super(connection);
        this.connection = connection;
        this.keyringManager = keyringManager;
        this.ackTracker = ackTracker;
    }


    /**
     * Signs a given payload.
     *
     * @param payload The payload to send.
     * @return A future that completes when the message has been sent.
     * @throws SignatureGenerationException If the message could not be signed.
     */
    private byte[] signPayload(byte[] payload) throws SignatureGenerationException
    {
        return CryptoUtils.sign(keyringManager.getMyPrivateKey(), payload);
    }


    /**
     * Encrypts a given payload.
     *
     * @param destination The destination of the message.
     * @param payload     The payload to send.
     * @return A future that completes when the message has been sent.
     * @throws MissingCertificateException    If the destination's certificate is missing.
     * @throws CertificateValidationException If the destination's certificate is invalid.
     * @throws KeyStoreException              If the keystore could not be accessed.
     * @throws EncryptionException            If the message could not be encrypted.
     */
    private byte[] encryptPayload(String destination, byte[] payload) throws MissingCertificateException, CertificateValidationException, KeyStoreException, EncryptionException
    {
        ECPublicKey publicKey = keyringManager.getPublicKey(destination);
        ECPrivateKey privateKey = keyringManager.getMyPrivateKey();
        return CryptoUtils.encryptMessage(publicKey, privateKey, payload);
    }


    /**
     * Creates a new SMMP message.
     *
     * @param payload     The payload to send.
     * @param signature   The signature of the payload.
     * @param encrypted   Whether the payload is encrypted.
     * @param requireAck  Whether an ack is required.
     * @param certificate The certificate of the sender.
     * @return The SMMP message.
     * @throws CertificateEncodingException If the certificate could not be encoded.
     * @throws SignatureGenerationException If the signature could not be generated.
     */
    private SMMPMessage createSMMPMessage(byte[] payload, byte[] signature, boolean encrypted, boolean requireAck, byte[] certificate) throws CertificateEncodingException, SignatureGenerationException
    {
        String SMMPMessageID = UUID.randomUUID().toString();
        return SMMPUtils.createMessage(payload, signature, SMMPMessageID, certificate, encrypted, requireAck);
    }


    private SMMPAckResponse createSMMPAckResponse(String messageId, byte[] signature, byte[] certificate)
    {
        return SMMPUtils.createAckResponse(messageId, signature, certificate);
    }


    /**
     * Sends a message to a single destination.
     *
     * @param destination The destination to send the message to.
     * @param payload     The payload to send.
     * @param expires     The time at which the message expires.
     * @param type        The type of the message.
     * @return A future that completes when the message has been sent.
     * @throws ConnectException If the underlying connection is not connected.
     */
    private CompletableFuture<String> sendInternal(String destination, byte[] payload, Instant expires, MessageType type) throws ConnectException
    {
        ProtocolMessage protocolMessage = ProtocolMessage.newBuilder().setType(type).setContent(ByteString.copyFrom(payload)).build();
        byte[] protocolMessageBytes = protocolMessage.toByteArray();
        return connection.sendDirect(destination, protocolMessageBytes, expires);
    }


    /**
     * Sends a message to multiple destinations.
     *
     * @param destinations The destinations to send the message to.
     * @param payload      The payload to send.
     * @param expires      The time at which the message expires.
     * @param type         The type of the message.
     * @return A future that completes when the message has been sent.
     * @throws ConnectException If the underlying connection is not connected.
     */
    private CompletableFuture<String> sendInternal(List<String> destinations, byte[] payload, Instant expires, MessageType type) throws ConnectException
    {
        ProtocolMessage protocolMessage = ProtocolMessage.newBuilder().setType(type).setContent(ByteString.copyFrom(payload)).build();
        byte[] protocolMessageBytes = protocolMessage.toByteArray();
        return connection.sendDirect(destinations, payload, expires);
    }


    private CompletableFuture<String> publishInternal(String subject, byte[] payload, Instant expires) throws ConnectException
    {
        ProtocolMessage protocolMessage = ProtocolMessage.newBuilder().setType(MessageType.MESSAGE).setContent(ByteString.copyFrom(payload)).build();
        byte[] protocolMessageBytes = protocolMessage.toByteArray();
        return connection.publish(subject, protocolMessageBytes, expires);
    }


    public void sendDirect(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt, @NonNull SingleDeliveryCompletionHandler handler) throws ConnectException
    {
        try
        {
            if (encrypt)
            {
                payload = encryptPayload(destination, payload);
            }

            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, encrypt, true, certificate);
            CompletableFuture<String> future = sendInternal(destination, message.toByteArray(), expires, MessageType.MESSAGE);

            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    handler.onFailure(ex);
                }

                else
                {
                    ackTracker.waitForSingleDestAck(message.getMessageID(), destination, message.toByteArray(), expires, this, handler);
                }
            });
        }

        catch (MissingCertificateException | CertificateEncodingException | SignatureGenerationException | EncryptionException | CertificateValidationException | KeyStoreException ex)
        {
            handler.onFailure(ex);
        }
    }


    public void sendDirect(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt, @NonNull SMMPSendingListener listener) throws ConnectException
    {
        try
        {
            if (encrypt)
            {
                payload = encryptPayload(destination, payload);
            }

            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, encrypt, false, certificate);

            CompletableFuture<String> future = sendInternal(destination, message.toByteArray(), expires, MessageType.MESSAGE);
            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    listener.onFailure(ex);
                }

                else
                {
                    listener.onSuccess();
                }
            });
        }

        catch (MissingCertificateException | CertificateEncodingException | SignatureGenerationException | EncryptionException | CertificateValidationException | KeyStoreException ex)
        {
            listener.onFailure(ex);
        }
    }


    public void sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires, @NonNull MultiDeliveryCompletionHandler handler) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, false, true, certificate);
            CompletableFuture<String> future = sendInternal(destinations, message.toByteArray(), expires, MessageType.MESSAGE);

            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    handler.onFailure(ex);
                }

                else
                {
                    ackTracker.waitForMultiDestAck(message.getMessageID(), destinations, message.toByteArray(), expires, this, handler);
                }
            });
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            handler.onFailure(ex);
        }
    }


    public void sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt, @NonNull SMMPSendingListener listener) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, encrypt, false, certificate);

            CompletableFuture<String> future = sendInternal(destinations, message.toByteArray(), expires, MessageType.MESSAGE);
            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    listener.onFailure(ex);
                }

                else
                {
                    listener.onSuccess();
                }
            });
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            listener.onFailure(ex);
        }
    }


    public void publish(@NonNull String subject, @NonNull byte[] payload, Instant expires, @NonNull SMMPSendingListener listener) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, false, false, certificate);

            CompletableFuture<String> future = publishInternal(subject, message.toByteArray(), expires);
            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    listener.onFailure(ex);
                }

                else
                {
                    listener.onSuccess();
                }
            });
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            listener.onFailure(ex);
        }
    }


    @Override
    public void resend(List<String> destinations, byte[] message, Instant expires)
    {
        try
        {
            sendInternal(destinations, message, expires, MessageType.MESSAGE);
        }
        catch (ConnectException ignored)
        {
        }
    }


    @Override
    public void ackResponse(String messageId, String destination)
    {
        try
        {
            byte[] signature = signPayload(messageId.getBytes());
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPAckResponse ackResponse = createSMMPAckResponse(messageId, signature, certificate);
            sendInternal(destination, ackResponse.toByteArray(), null, MessageType.ACKRESPONSE);
        }

        catch (ConnectException | SignatureGenerationException | CertificateEncodingException ignored)
        {

        }
    }
}