package SMMPClient.Adapters;

import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AnonymousConnection;
import Agent.Connections.AuthenticatedConnection;
import SMMPClient.Acks.AckTracker;
import SMMPClient.Connections.SMMPAuthConnection;
import SMMPClient.Crypto.CryptoUtils;
import SMMPClient.Crypto.IKeyringManager;
import SMMPClient.Exceptions.CertificateValidationException;
import SMMPClient.Exceptions.DecryptionException;
import SMMPClient.Exceptions.MissingCertificateException;
import SMMPClient.Exceptions.SignatureVerificationException;
import SMMPClient.MessageFormats.MessageType;
import SMMPClient.MessageFormats.ProtocolMessage;
import SMMPClient.MessageFormats.SMMPAck;
import SMMPClient.MessageFormats.SMMPMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * A SMMP specific implementation of the authenticated adapter interface provided by the agent.
 * This class is responsible for handling the messages received from the agent and passing them to the SMMP adapter
 * after they have been validated and verified.
 */
@Slf4j
public class AuthenticatedAdapterImpl implements AuthenticatedAdapter
{
    private final Set<String> deliveredToApplication = new ConcurrentSkipListSet<>();
    private final IKeyringManager keyringManager;
    private final SMMPAuthAdapter adapter;
    private AckTracker ackTracker;


    /**
     * Creates a new instance of the {@link AuthenticatedAdapterImpl} an SMMP specific implementation of {@link AuthenticatedAdapter}
     *
     * @param keyringManager The {@link IKeyringManager} manager used to verify the signatures of the messages
     * @param adapter        The {@link SMMPAuthAdapter} that will receive the callbacks.
     */
    public AuthenticatedAdapterImpl(@NonNull IKeyringManager keyringManager, @NonNull SMMPAuthAdapter adapter)
    {
        this.keyringManager = keyringManager;
        this.adapter = adapter;
    }



    /**
     * Called when the agent has successfully connected to the edge router in anonymous mode.
     * Wraps the connection in a SMMP specific connection and passes it to the SMMP adapter.
     * <p>
     * Calls the {@link SMMPAuthAdapter} with a {@link SMMPClient.Connections.ISMMPAuthConnection} as a argument.
     *
     * @param connection The {@link AnonymousConnection} that was established.
     */
    @Override
    public void onConnect(AuthenticatedConnection connection)
    {
        log.info("Connected to edge router in authenticated mode");
        this.ackTracker = new AckTracker(connection, keyringManager.getMyCertificate(), keyringManager.getMyPrivateKey());
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
            log.debug("Received direct MMTP-Message={} from {} to destinations {}", messageId, sender, destinations);
            ProtocolMessage protocolMessage = ProtocolMessage.parseFrom(message);

            if (protocolMessage.getType() == MessageType.MESSAGE)
            {
                log.debug("MMTP-Message={} contained a {}", messageId, MessageType.MESSAGE);
                processDirectMessage(protocolMessage, sender, destinations, expires);
            }

            else if (protocolMessage.getType() == MessageType.ACK)
            {
                log.debug("MMTP-Message={} contained a {}", messageId, MessageType.ACK);
                processAck(protocolMessage, sender);
            }

            else if (protocolMessage.getType() == MessageType.UNRECOGNIZED)
            {
                log.warn("MMTP-Message={} contained a unrecognized message={}", messageId, MessageType.UNRECOGNIZED);
            }
        }

        catch (InvalidProtocolBufferException | DecryptionException | MissingCertificateException | CertificateValidationException | CertificateException | SignatureVerificationException |
               KeyStoreException ex)
        {
            log.warn("Error while processing message={}", messageId, ex);
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
            log.debug("Received subject cast MMTP-Message={} from {} with subject {}", messageId, sender, subject);
            ProtocolMessage protocolMessage = ProtocolMessage.parseFrom(message);

            if (protocolMessage.getType() == MessageType.MESSAGE)
            {
                log.debug("MMTP-Message{} contained a {}", messageId, MessageType.MESSAGE);
                processSubjectCastMessage(protocolMessage, sender, subject, expires);
            }

            else
            {
                log.warn("MMTP-Message={} contain a a message with type ({}). Ignoring message, only {} is supported over subject cast", messageId, protocolMessage.getType(), MessageType.MESSAGE);
            }
        }

        catch (InvalidProtocolBufferException | CertificateException | MissingCertificateException | CertificateValidationException | SignatureVerificationException | KeyStoreException ex)
        {
            log.warn("Error while processing subject cast MMTP-Message={} from sender {}", messageId, sender, ex);
        }
    }


    /**
     * Called once the agent has been disconnected from the edge router.
     * Calls the {@link SMMPAnonAdapter} with the disconnect code and phrase.
     *
     * @param statusCode the status code of the disconnection
     * @param reason     the reason for the disconnection
     */
    @Override
    public void onDisconnect(int statusCode, String reason)
    {
        log.info("Disconnected from edge router with status code {} and reason: {}", statusCode, reason);
        adapter.onDisconnect(statusCode, reason);
    }


    /**
     * Called when the agent has encountered an error while connecting to the edge router.
     * Calls the {@link SMMPAuthAdapter} with the connection error that occurred.
     *
     * @param ex The throwable that was thrown
     */
    @Override
    public void onConnectionError(Throwable ex)
    {
        log.error("Error occurred while trying to connect to edge router", ex);
        adapter.onConnectionError(ex);
    }


    /**
     * Processes a message. Verifies the signature, decrypts and sends the message to the SMMP adapter if the signature is valid.
     *Calls the {@link SMMPAuthAdapter} if the message is valid and trustworthy.
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
    private void processDirectMessage(ProtocolMessage protocolMessage, String sender, List<String> destinations, Instant expires) throws InvalidProtocolBufferException, CertificateException, KeyStoreException, MissingCertificateException, CertificateValidationException, SignatureVerificationException, DecryptionException
    {
        SMMPMessage smmpMessage = SMMPMessage.parseFrom(protocolMessage.getContent());
        String messageId = smmpMessage.getMessageID();
        byte[] signature = smmpMessage.getSignature().toByteArray();
        byte[] content = smmpMessage.getPayload().toByteArray();
        X509Certificate certificate = getCertificate(smmpMessage.getCertificate().toByteArray());

        if (!verifyCertificate(certificate, sender))
        {
            log.warn("Received a direct SMMP-message={} from an untrusted sender: {}, ignoring", messageId, sender);
            return;
        }

        if (smmpMessage.getIsEncrypted())
        {
            log.debug("SMMP-message={} is encrypted, decrypting..", messageId);
            content = CryptoUtils.decryptMessage((ECPublicKey) certificate.getPublicKey(), keyringManager.getMyPrivateKey(), content);
        }

        if (!CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), content, signature))
        {
            log.warn("Received a direct SMMP-message={} with an invalid signature from sender: {}, ignoring", messageId, sender);
            return;
        }

        if (smmpMessage.getRequiresAck())
        {
            log.warn("Received a direct SMMP-message={} that requires ack from sender: {}, sending ack", messageId, sender);
            ackTracker.sendAck(messageId, sender);
        }

        if (deliveredToApplication.contains(messageId))
        {
            log.warn("Message already delivered to application, likely a retransmit.");
            return;
        }

        log.debug("Direct SMMP-message={} valid, passing to application", messageId);
        deliveredToApplication.add(messageId);
        adapter.onDirectMessage(smmpMessage.getMessageID(), destinations, sender, expires, content);
    }


    /**
     * Processes an ack message. Verifies the signature and passes the ack to the ack tracker if the signature is valid.
     * Calls the {@link SMMPClient.Acks.IAckTracker} to handle the ack.
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
            log.warn("Received a SMMP-ack for SMMP-Message{} from an untrusted sender: {}, ignoring", ackedMessageId, sender);
            return;
        }

        if (!CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), smmpAck.getMessageID().getBytes(), signature))
        {
            log.warn("Received a SMMP-ack for SMMP-Message{} with an invalid signature from sender: {}, ignoring", ackedMessageId, sender);
            return;
        }

        log.debug("SMMP-Ack for SMMP-Message={} valid, passing to ACK-HANDLER", ackedMessageId);
        ackTracker.acknowledge(ackedMessageId, sender);
    }


    /**
     * Processes a subject cast message. Verifies the signature and sends the message to the SMMP adapter if the signature is valid.
     *Calls the {@link SMMPAuthAdapter} if the message is valid and trustworthy.
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
    private void processSubjectCastMessage(ProtocolMessage protocolMessage, String sender, String subject, Instant expires) throws MissingCertificateException, CertificateValidationException, KeyStoreException, InvalidProtocolBufferException, SignatureVerificationException, CertificateException
    {
        SMMPMessage smmpMessage = SMMPMessage.parseFrom(protocolMessage.getContent());
        String messageId = smmpMessage.getMessageID();
        byte[] signature = smmpMessage.getSignature().toByteArray();
        byte[] content = smmpMessage.getPayload().toByteArray();
        X509Certificate certificate = getCertificate(smmpMessage.getCertificate().toByteArray());

        if (!verifyCertificate(certificate, sender))
        {
            log.warn("Received a subject cast SMMP-message={} from an untrusted sender: {}, ignoring", messageId, sender);
            return;
        }

        if (smmpMessage.getIsEncrypted())
        {
            log.warn("Received a subject cast SMMP-message={} with encryption from sender: {}, ignoring", messageId, sender);
            return;
        }


        if (!CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), content, signature))
        {
            log.warn("Received a subject cast SMMP-message={} with an invalid signature from sender: {}, ignoring", messageId, sender);
            return;
        }

        if (deliveredToApplication.contains(messageId))
        {
            log.warn("Message already delivered to application, likely a retransmit.");
            return;
        }

        log.debug("Subject cast SMMP-message={} valid, passing to application", messageId);
        deliveredToApplication.add(messageId);
        adapter.onSubjectCastMessage(smmpMessage.getMessageID(), sender, subject, expires, content);
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