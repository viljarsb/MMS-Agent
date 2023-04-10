package SMMPClient.Connections;


import Agent.Connections.IAnonymousConnection;
import Agent.Connections.IAuthenticatedConnection;
import Agent.Exceptions.ConnectException;
import SMMPClient.Acks.*;
import SMMPClient.Acks.Handlers.MultiDeliveryCompletionHandler;
import SMMPClient.Acks.Handlers.SMMPSendingHandler;
import SMMPClient.Acks.Handlers.SingleDeliveryCompletionHandler;
import SMMPClient.Acks.ResultObjects.MultiDestDeliveryResult;
import SMMPClient.Acks.ResultObjects.SingleDestDeliveryResult;
import SMMPClient.Crypto.CryptoUtils;
import SMMPClient.Crypto.IKeyringManager;
import SMMPClient.Exceptions.EncryptionException;
import SMMPClient.Exceptions.SignatureGenerationException;
import SMMPClient.Exceptions.CertificateValidationException;
import SMMPClient.Exceptions.MissingCertificateException;
import SMMPClient.MessageFormats.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * A wrapper around an {@link Agent.Connections.IAuthenticatedConnection} that provides a more convenient interface for SMMP.
 * This wrapper really doesn't do much, but it does make the API a little more intuitive,
 * and users of this library don't need to know about the Agent library.
 */
@Slf4j
public class SMMPAuthConnection extends SMMPAnonConnection implements ISMMPAuthConnection
{
    private final IAuthenticatedConnection connection;
    private final IKeyringManager keyringManager;
    private final IAckTracker ackTracker;


    /**
     * Creates a new {@link SMMPAuthConnection}.
     *
     * @param connection {@link IAuthenticatedConnection} to wrap
     * @param  keyringManager {@link IKeyringManager} keyring manager
     */
    public SMMPAuthConnection(@NonNull IAuthenticatedConnection connection, @NonNull IKeyringManager keyringManager, @NonNull IAckTracker ackTracker)
    {
        super(connection);
        this.connection = connection;
        this.keyringManager = keyringManager;
        this.ackTracker = ackTracker;
    }


    public void sendDirectWithAcknowledgement(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt, @NonNull SingleDeliveryCompletionHandler handler) throws ConnectException
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
            CompletableFuture<String> future = sendInternal(destination, message.toByteArray(), expires);

            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    log.error("Error sending SMMP message to destination: {}. Exception: ", destination, ex);
                    handler.onFailure(ex);
                }
                else
                {
                    log.debug("Waiting for acknowledgement from destination: {} for message ID: {}", destination, message.getMessageID());
                    ackTracker.waitForSingleDestAck(message.getMessageID(), destination, message.toByteArray(), expires, handler);
                }
            });
        }
        catch (MissingCertificateException | CertificateEncodingException | SignatureGenerationException | EncryptionException | CertificateValidationException | KeyStoreException ex)
        {
            log.error("Error while sending SMMP message with acknowledgement to destination: {}. Exception: ", destination, ex);
            handler.onFailure(ex);
        }
    }


    public CompletableFuture<SingleDestDeliveryResult> sendDirectWithAcknowledgement(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt) throws ConnectException
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
            CompletableFuture<String> future = sendInternal(destination, message.toByteArray(), expires);
            return future.thenComposeAsync((messageID) -> ackTracker.waitForSingleDestAck(message.getMessageID(), destination, message.toByteArray(), expires));

        }
        catch (MissingCertificateException | CertificateEncodingException | SignatureGenerationException | EncryptionException | CertificateValidationException | KeyStoreException ex)
        {
            log.error("Error while sending SMMP message with acknowledgement to destination: {}. Exception: ", destination, ex);
            return CompletableFuture.failedFuture(ex);
        }
    }


    public CompletableFuture<Boolean> sendDirect(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt) throws ConnectException
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
            CompletableFuture<String> future = sendInternal(destination, message.toByteArray(), expires);
            return future.thenApply((messageID) -> true);
        }

        catch (MissingCertificateException | CertificateEncodingException | SignatureGenerationException | EncryptionException | CertificateValidationException | KeyStoreException ex)
        {
            return CompletableFuture.failedFuture(ex);
        }
    }


    public void sendDirect(@NonNull String destination, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt, @NonNull SMMPSendingHandler listener) throws ConnectException
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

            CompletableFuture<String> future = sendInternal(destination, message.toByteArray(), expires);
            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    log.error("Failed to send direct message to {} with error {}", destination, ex.getMessage());
                    listener.onFailure(ex);
                }

                else
                {
                    log.info("Direct message sent to {} with message ID {}", destination, messageID);
                    listener.onSuccess();
                }
            });
        }

        catch (MissingCertificateException | CertificateEncodingException | SignatureGenerationException | EncryptionException | CertificateValidationException | KeyStoreException ex)
        {
            log.error("Failed to send direct message to {} with error {}", destination, ex.getMessage());
            listener.onFailure(ex);
        }
    }


    public void sendDirectWithAcknowledgement(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires, @NonNull MultiDeliveryCompletionHandler handler) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, false, true, certificate);
            CompletableFuture<String> future = sendInternal(destinations, message.toByteArray(), expires);

            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    log.error("Failed to send direct message to {} with error {}", destinations, ex.getMessage());
                    handler.onFailure(ex);
                }

                else
                {
                    log.info("Direct message sent to {} with message ID {}", destinations, messageID);
                    ackTracker.waitForMultiDestAck(message.getMessageID(), destinations, message.toByteArray(), expires, handler);
                }
            });
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            log.error("Failed to send direct message to {} with error {}", destinations, ex.getMessage());
            handler.onFailure(ex);
        }
    }


    public CompletableFuture<MultiDestDeliveryResult> sendDirectWithAcknowledgement(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, false, true, certificate);
            CompletableFuture<String> future = sendInternal(destinations, message.toByteArray(), expires);

            return future.thenComposeAsync((messageID) ->
            {
                log.info("Direct message sent to {} with message ID {}", destinations, messageID);
                return ackTracker.waitForMultiDestAck(message.getMessageID(), destinations, message.toByteArray(), expires);
            });
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            log.error("Failed to send message to destinations: {}. Error: {}", destinations, ex.getMessage());
            return CompletableFuture.failedFuture(ex);
        }
    }


    public CompletableFuture<Boolean> sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, false, false, certificate);
            CompletableFuture<String> future = sendInternal(destinations, message.toByteArray(), expires);
            log.info("Successfully sent SMMP message directly to destinations: {}, message ID: {}", destinations, future.join());
            return future.thenApply((messageID) -> true);
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            log.error("Error occurred while sending SMMP message directly to destinations: {}", destinations, ex);
            return CompletableFuture.failedFuture(ex);
        }
    }


    public void sendDirect(@NonNull List<String> destinations, @NonNull byte[] payload, Instant expires, @NonNull Boolean encrypt, @NonNull SMMPSendingHandler listener) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, encrypt, false, certificate);

            CompletableFuture<String> future = sendInternal(destinations, message.toByteArray(), expires);
            future.whenComplete((messageID, ex) ->
            {
                if (ex != null)
                {
                    log.error("Error occurred while sending SMMP message directly to destinations: {}", destinations, ex);
                    listener.onFailure(ex);
                }

                else
                {
                    log.info("Successfully sent SMMP message directly to destinations: {}, message ID: {}", destinations, messageID);
                    listener.onSuccess();
                }
            });
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            log.error("Error occurred while sending SMMP message directly to destinations: {}", destinations, ex);
            listener.onFailure(ex);
        }
    }


    public void publish(@NonNull String subject, @NonNull byte[] payload, Instant expires, @NonNull SMMPSendingHandler listener) throws ConnectException
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
                    log.error("Error occurred while publishing SMMP message to subject: {}", subject, ex);
                    listener.onFailure(ex);
                }

                else
                {
                    log.info("Successfully published SMMP message to subject: {}, message ID: {}", subject, messageID);
                    listener.onSuccess();
                }
            });
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            log.error("Error occurred while publishing SMMP message to subject: {}", subject, ex);
            listener.onFailure(ex);
        }
    }


    @Override
    public CompletableFuture<Boolean> publish(String subject, byte[] payload, Instant expires) throws ConnectException
    {
        try
        {
            byte[] signature = signPayload(payload);
            byte[] certificate = keyringManager.getMyCertificate().getEncoded();
            SMMPMessage message = createSMMPMessage(payload, signature, false, false, certificate);
            CompletableFuture<String> future = publishInternal(subject, message.toByteArray(), expires);
            log.info("Successfully published SMMP message to subject: {}, message ID: {}", subject, future.join());
            return future.thenApply((messageID) -> true);
        }

        catch (CertificateEncodingException | SignatureGenerationException ex)
        {
            log.error("Error occurred while publishing SMMP message to subject: {}", subject, ex);
            return CompletableFuture.failedFuture(ex);
        }
    }


    private byte[] signPayload(byte[] payload) throws SignatureGenerationException
    {
        return CryptoUtils.sign(keyringManager.getMyPrivateKey(), payload);
    }


    private byte[] encryptPayload(String destination, byte[] payload) throws MissingCertificateException, CertificateValidationException, KeyStoreException, EncryptionException
    {
        ECPublicKey publicKey = keyringManager.getPublicKey(destination);
        ECPrivateKey privateKey = keyringManager.getMyPrivateKey();
        return CryptoUtils.encryptMessage(publicKey, privateKey, payload);
    }


    private SMMPMessage createSMMPMessage(byte[] payload, byte[] signature, boolean encrypted, boolean requireAck, byte[] certificate) throws CertificateEncodingException, SignatureGenerationException
    {
        String SMMPMessageID = UUID.randomUUID().toString();
        return SMMPUtils.createMessage(payload, signature, SMMPMessageID, certificate, encrypted, requireAck);
    }


    private CompletableFuture<String> sendInternal(String destination, byte[] payload, Instant expires) throws ConnectException
    {
        byte[] protocolMessageBytes = SMMPUtils.createProtocolMessage(payload, MessageType.MESSAGE).toByteArray();
        return connection.sendDirect(destination, protocolMessageBytes, expires);
    }


    private CompletableFuture<String> sendInternal(List<String> destinations, byte[] payload, Instant expires) throws ConnectException
    {
        byte[] protocolMessageBytes = SMMPUtils.createProtocolMessage(payload, MessageType.MESSAGE).toByteArray();
        return connection.sendDirect(destinations, protocolMessageBytes, expires);
    }


    private CompletableFuture<String> publishInternal(String subject, byte[] payload, Instant expires) throws ConnectException
    {
        byte[] protocolMessageBytes = SMMPUtils.createProtocolMessage(payload, MessageType.MESSAGE).toByteArray();
        return connection.publish(subject, protocolMessageBytes, expires);
    }
}