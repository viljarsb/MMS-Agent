package SMMPClient.Adapters;


import Agent.Agent.AnonymousAdapter;
import Agent.Connections.AnonymousConnection;
import SMMPClient.Connections.SMMPAnonConnection;
import SMMPClient.Crypto.CryptoUtils;
import SMMPClient.Crypto.IKeyringManager;
import SMMPClient.Exceptions.CertificateValidationException;
import SMMPClient.Exceptions.MissingCertificateException;
import SMMPClient.Exceptions.SignatureVerificationException;
import SMMPClient.MessageFormats.MessageType;
import SMMPClient.MessageFormats.ProtocolMessage;
import SMMPClient.MessageFormats.SMMPMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A SMMP specific implementation of the anonymous adapter interface provided by the agent.
 * This class is responsible for handling the messages received from the agent and passing them to the SMMP adapter
 * after they have been validated and verified.
 */
@Slf4j
public class AnonymousAdapterImpl implements AnonymousAdapter
{
    private final Set<String> deliveredToApplication = new ConcurrentSkipListSet<>();
    private final IKeyringManager keyringManager;
    private final SMMPAnonAdapter adapter;


    /**
     * Creates a new instance of the {@link AnonymousAdapterImpl} an SMMP specific implementation of {@link AnonymousAdapter}
     *
     * @param keyringManager The {@link IKeyringManager} manager used to verify the signatures of the messages
     * @param adapter        The {@link SMMPAnonAdapter} that will receive the callbacks.
     */
    public AnonymousAdapterImpl(@NonNull IKeyringManager keyringManager, @NonNull SMMPAnonAdapter adapter)
    {
        this.keyringManager = keyringManager;
        this.adapter = adapter;
    }


    /**
     * Called when the agent has successfully connected to the edge router in anonymous mode.
     * Wraps the connection in a SMMP specific connection and passes it to the SMMP adapter.
     * <p>
     * Calls the {@link SMMPAnonAdapter} with a {@link SMMPClient.Connections.ISMMPAnonConnection} as a argument.
     *
     * @param connection The {@link AnonymousConnection} that was established.
     */
    @Override
    public void onConnect(@NonNull AnonymousConnection connection)
    {
        log.info("Connected to edge router in anonymous mode");
        adapter.onConnect(new SMMPAnonConnection(connection));
    }


    /**
     * Called once the agent has received a message from the edge router (subject cast message).
     * Passes for further processing if the message is a SMMP message.
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
                log.debug("MMTP-Message={} contained a {}", messageId, MessageType.MESSAGE);
                processMessage(protocolMessage, sender, subject, expires);
            }

            else
            {
                log.warn("MMTP-Message={} contain a a message with type ({}). Ignoring message, only {} is supported over subject cast", messageId, protocolMessage.getType(), MessageType.MESSAGE);
            }
        }
        catch (InvalidProtocolBufferException | MissingCertificateException | CertificateValidationException | CertificateException | SignatureVerificationException | KeyStoreException ex)
        {
            log.warn("Error while processing subject cast MMTP-Message={} from sender {}", messageId, sender, ex);
        }
    }


    /**
     * Called when the agent has disconnected from the edge router.
     * Calls the {@link SMMPAnonAdapter} with the disconnect code and phrase.
     *
     * @param statusCode The status code of the disconnection
     * @param reason     The reason for the disconnection
     */
    @Override
    public final void onDisconnect(int statusCode, String reason)
    {
        log.warn("Disconnected from the anonymous connection with status code: {} and reason: {}", statusCode, reason);
        adapter.onDisconnect(statusCode, reason);
    }


    /**
     * Called when the agent has encountered an error while connecting to the edge router.
     * Calls the {@link SMMPAnonAdapter} with the connection error that occurred.
     *
     * @param ex The throwable that was thrown
     */
    @Override
    public final void onConnectionError(Throwable ex)
    {
        log.warn("Error while connecting to the edge router in anonymous mode", ex);
        adapter.onConnectionError(ex);
    }


    /**
     * Processes a SMMP message by verifying the signature and passing it to the SMMP adapter if everything is valid.
     * Calls the {@link SMMPAnonAdapter} if the message is valid and trustworthy.
     *
     * @param protocolMessage The message to process
     * @param sender          The sender of the message
     * @param subject         The subject of the message
     * @param expires         The expiration time of the message
     * @throws InvalidProtocolBufferException If the message is not a valid protocol message
     * @throws CertificateException           If the certificate is not a valid X.509 certificate
     * @throws KeyStoreException              If the key store is not initialized
     * @throws MissingCertificateException    If the certificate is not found in the key store
     * @throws CertificateValidationException If the certificate is not valid
     * @throws SignatureVerificationException If the signature is not valid
     */
    private void processMessage(ProtocolMessage protocolMessage, String sender, String subject, Instant expires) throws InvalidProtocolBufferException, CertificateException, KeyStoreException, MissingCertificateException, CertificateValidationException, SignatureVerificationException
    {
        SMMPMessage smmpMessage = SMMPMessage.parseFrom(protocolMessage.getContent());
        String messageId = smmpMessage.getMessageID();
        byte[] signature = smmpMessage.getSignature().toByteArray();
        byte[] content = smmpMessage.getPayload().toByteArray();
        X509Certificate certificate = getCertificate(smmpMessage.getCertificate().toByteArray());

        if (smmpMessage.getIsEncrypted())
        {
            log.warn("Received an encrypted subject cast SMMP-message={} from sender {} with subject {}. Ignoring message", messageId, sender, subject);
            return;
        }

        if (!verifyCertificate(certificate, sender))
        {
            log.warn("Received a message subject cast SMMP-message={} from an untrusted sender: {} with subject {}. Ignoring message", messageId, sender, subject);
            return;
        }

        if (!CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), content, signature))
        {
            log.warn("Failed to verify the signature of subject cast SMMP-message={} from sender {} with subject {}. Ignoring the message.", messageId, sender, subject);
            return;
        }

        if (deliveredToApplication.contains(messageId))
        {
            log.warn("Message already delivered to application, likely a retransmit.");
            return;
        }

        log.debug("SMMP-message={} valid, passing to application", messageId);
        deliveredToApplication.add(messageId);
        adapter.onSubjectCastMessage(messageId, sender, subject, expires, content);
    }


    /**
     * Converts a byte array to an X.509 certificate
     *
     * @param certificateBytes The byte array to convert
     * @return The X.509 certificate
     * @throws CertificateException If the certificate is not a valid X.509 certificate
     */
    private X509Certificate getCertificate(byte[] certificateBytes) throws CertificateException
    {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
    }


    /**
     * Verifies the certificate by checking if it is trusted and if the public key matches the sender
     *
     * @param certificate The certificate to verify
     * @param sender      The sender of the message
     * @return True if the certificate is trusted and the public key matches the sender, false otherwise
     * @throws KeyStoreException              If the key store is not initialized
     * @throws MissingCertificateException    If the certificate is not found in the key store
     * @throws CertificateValidationException If the certificate is not valid
     */
    private boolean verifyCertificate(X509Certificate certificate, String sender) throws KeyStoreException, MissingCertificateException, CertificateValidationException
    {
        return keyringManager.verifyCertificate(certificate) && certificate.getPublicKey().equals(keyringManager.getPublicKey(sender));
    }
}