package SMMPClient.Adapters;

import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AuthenticatedConnection;
import SMMPClient.Acks.AckTracker;
import SMMPClient.Crypto.CryptoUtils;
import SMMPClient.Crypto.KeyringManager;
import SMMPClient.Exceptions.CryptoExceptions.DecryptionException;
import SMMPClient.Exceptions.CryptoExceptions.SignatureVerificationException;
import SMMPClient.Exceptions.PkiExceptions.CertificateValidationException;
import SMMPClient.Exceptions.PkiExceptions.MissingCertificateException;
import SMMPClient.MessageFormats.MessageType;
import SMMPClient.MessageFormats.ProtocolMessage;
import SMMPClient.MessageFormats.SMMPAck;
import SMMPClient.MessageFormats.SMMPMessage;
import SMMPClient.Connections.SMMPAuthConnection;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;
import java.util.List;


/**
 * A SMMP specific implementation of the authenticated adapter interface provided by the agent.
 * This class is responsible for handling the messages received from the agent and passing them to the SMMP adapter
 * after they have been validated and verified.
 */
@Slf4j
public class AuthenticatedAdapterImpl implements AuthenticatedAdapter
{
    private final KeyringManager keyringManager;
    private final SMMPAuthAdapter adapter;
    private final AckTracker ackTracker;


    /**
     * Constructor for the authenticated adapter implementation.
     *
     * @param keyringManager The keyring manager used to verify and decrypt the messages.
     * @param adapter        The SMMP adapter that will receive the callbacks.
     */
    public AuthenticatedAdapterImpl(KeyringManager keyringManager, SMMPAuthAdapter adapter)
    {
        this.keyringManager = keyringManager;
        this.adapter = adapter;
        this.ackTracker = new AckTracker();
    }


    /**
     * Called when the agent has successfully connected to the edge router in authenticated mode.
     * Wraps the connection in a SMMP specific connection and passes it to the SMMP adapter.
     *
     * @param connection The connection that was established.
     */
    @Override
    public void onConnect(AuthenticatedConnection connection)
    {
        adapter.onConnect(new SMMPAuthConnection(connection, keyringManager, ackTracker));
    }


    /**
     * Called once the agent has received a message from the edge router (directed message).
     * Parses the message and sends it for further processing if it is a SMMP message or an ack.
     *
     * @param messageId    the ID of the message
     * @param destinations the destinations of the message
     * @param sender       the sender of the message
     * @param expires      the expiration time of the message
     * @param message      the message content
     */
    @Override
    public void onDirectMessage(String messageId, List<String> destinations, String sender, Instant expires, byte[] message)
    {
        try
        {
            ProtocolMessage protocolMessage = ProtocolMessage.parseFrom(message);

            if (protocolMessage.getType() == MessageType.MESSAGE)
            {
                processMessage(protocolMessage, sender, destinations, expires);
            }

            else if (protocolMessage.getType() == MessageType.ACK)
            {
                processAck(protocolMessage, sender);
            }
        }

        catch (InvalidProtocolBufferException | DecryptionException | MissingCertificateException | CertificateValidationException | CertificateException | SignatureVerificationException |
               KeyStoreException e)
        {
            log.error("Error while processing message", e);
        }
    }


    /**
     * Called once the agent has received a message from the edge router (subject cast message).
     * Parses the message and sends it for further processing if it is a SMMP message.
     *
     * @param messageId the ID of the message
     * @param sender    the sender of the message
     * @param subject   the subject of the message
     * @param expires   the expiration time of the message
     * @param message   the message content
     */
    @Override
    public void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message)
    {
        try
        {
            ProtocolMessage protocolMessage = ProtocolMessage.parseFrom(message);

            if (protocolMessage.getType() == MessageType.MESSAGE)
            {
                processSubjectCastMessage(protocolMessage, sender, subject, expires);
            }
        }

        catch (InvalidProtocolBufferException | MissingCertificateException | CertificateValidationException | SignatureVerificationException | KeyStoreException e)
        {
            log.error("Error while processing message", e);
        }
    }


    /**
     * Called once the agent has been disconnected from the edge router.
     *
     * @param statusCode the status code of the disconnection
     * @param reason     the reason for the disconnection
     */
    @Override
    public void onDisconnect(int statusCode, String reason)
    {
        adapter.onDisconnect(statusCode, reason);
    }


    /**
     * Called once the agent has encountered an error while trying to connect to the edge router.
     *
     * @param ex the throwable that was thrown
     */
    @Override
    public void onConnectionError(Throwable ex)
    {
        adapter.onConnectionError(ex);
    }


    /**
     * Processes a subject cast message. Verifies the signature and sends the message to the SMMP adapter if the signature is valid.
     *
     * @param protocolMessage the protocol message
     * @param sender          the sender of the message
     * @param subject         the subject of the message
     * @param expires         the expiration time of the message
     * @throws MissingCertificateException    If the sender's certificate is missing from the keyring.
     * @throws CertificateValidationException If the sender's certificate is invalid.
     * @throws KeyStoreException              If the keyring is not initialized.
     * @throws InvalidProtocolBufferException If the protocol message is invalid.
     * @throws SignatureVerificationException If the signature is invalid.
     */
    private void processSubjectCastMessage(ProtocolMessage protocolMessage, String sender, String subject, Instant expires) throws MissingCertificateException, CertificateValidationException, KeyStoreException, InvalidProtocolBufferException, SignatureVerificationException
    {
        SMMPMessage smmpMessage = SMMPMessage.parseFrom(protocolMessage.getContent());
        byte[] signature = smmpMessage.getSignature().toByteArray();
        byte[] content = smmpMessage.getPayload().toByteArray();

        if (smmpMessage.getIsEncrypted())
        {
            log.error("Received an encrypted subject cast message from sender: {}", sender);
            return;
        }

        if (CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), content, signature))
        {
            adapter.onSubjectCastMessage(smmpMessage.getMessageID(), sender, subject, expires, content);
        }
    }


    /**
     * Processes a message. Verifies the signature, decrypts and sends the message to the SMMP adapter if the signature is valid.
     *
     * @param protocolMessage the protocol message
     * @param sender          the sender of the message
     * @param destinations    the destinations of the message
     * @param expires         the expiration time of the message
     * @throws InvalidProtocolBufferException If the protocol message is invalid.
     * @throws CertificateException           If the sender's certificate is invalid.
     * @throws KeyStoreException              If the keyring is not initialized.
     * @throws MissingCertificateException    If the sender's certificate is missing from the keyring.
     * @throws CertificateValidationException If the sender's certificate is invalid.
     * @throws SignatureVerificationException If the signature is invalid.
     * @throws DecryptionException            If the message could not be decrypted.
     */
    private void processMessage(ProtocolMessage protocolMessage, String sender, List<String> destinations, Instant expires) throws InvalidProtocolBufferException, CertificateException, KeyStoreException, MissingCertificateException, CertificateValidationException, SignatureVerificationException, DecryptionException
    {
        SMMPMessage smmpMessage = SMMPMessage.parseFrom(protocolMessage.getContent());
        byte[] signature = smmpMessage.getSignature().toByteArray();
        byte[] content = smmpMessage.getPayload().toByteArray();
        X509Certificate certificate = getCertificate(smmpMessage.getCertificate().toByteArray());

        if (!verifyCertificate(certificate, sender))
        {
            log.warn("Received a message from an untrusted sender: {}", sender);
            return;
        }

        if (smmpMessage.getIsEncrypted())
        {
            content = CryptoUtils.decryptMessage((ECPublicKey) certificate.getPublicKey(), keyringManager.getMyPrivateKey(), content);
        }

        if (CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), content, signature))
        {
            adapter.onDirectMessage(smmpMessage.getMessageID(), destinations, sender, expires, content);
            log.info("Successfully verified the signature of the message from sender: {}", sender);
        }

        else
        {
            log.error("Signature verification failed for message from sender: {}", sender);
        }
    }


    /**
     * Processes an ack message. Verifies the signature and passes the ack to the ack tracker if the signature is valid.
     *
     * @param protocolMessage the protocol message
     * @param sender          the sender of the message
     * @throws InvalidProtocolBufferException If the protocol message is invalid.
     * @throws CertificateException           If the sender's certificate is invalid.
     * @throws KeyStoreException              If the keyring is not initialized.
     * @throws MissingCertificateException    If the sender's certificate is missing from the keyring.
     * @throws CertificateValidationException If the sender's certificate is invalid.
     * @throws SignatureVerificationException If the signature is invalid.
     */
    private void processAck(ProtocolMessage protocolMessage, String sender) throws InvalidProtocolBufferException, CertificateException, KeyStoreException, MissingCertificateException, CertificateValidationException, SignatureVerificationException
    {
        SMMPAck smmpAck = SMMPAck.parseFrom(protocolMessage.getContent());
        byte[] signature = smmpAck.getSignature().toByteArray();
        String ackedMessageId = smmpAck.getMessageID();
        X509Certificate certificate = getCertificate(smmpAck.getCertificate().toByteArray());

        if (!verifyCertificate(certificate, sender))
        {
            log.warn("Received a message from an untrusted sender: {}", sender);
            return;
        }

        if (CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), smmpAck.getMessageID().getBytes(), signature))
        {
            log.info("Successfully verified the signature of the message from sender: {}", sender);
            ackTracker.acknowledge(ackedMessageId, sender);
        }

        else
        {
            log.error("Signature verification failed for message from sender: {}", sender);
        }
    }


    /**
     * Gets the certificate from the byte array.
     *
     * @param certificateBytes the certificate bytes
     * @return the certificate
     * @throws CertificateException if the certificate is invalid
     */
    private X509Certificate getCertificate(byte[] certificateBytes) throws CertificateException
    {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
    }


    /**
     * Verifies the certificate.
     *
     * @param certificate the certificate
     * @param sender      the sender
     * @return true if the certificate is valid and the public key matches the sender's public key
     * @throws KeyStoreException              If the keyring is not initialized.
     * @throws MissingCertificateException    If the sender's certificate is missing from the keyring.
     * @throws CertificateValidationException If the sender's certificate is invalid.
     */
    private boolean verifyCertificate(X509Certificate certificate, String sender) throws KeyStoreException, MissingCertificateException, CertificateValidationException
    {
        return keyringManager.verifyCertificate(certificate) && certificate.getPublicKey().equals(keyringManager.getPublicKey(sender));
    }
}