package SMMPClient.Adapters;


import Agent.Agent.AnonymousAdapter;
import Agent.Connections.AnonymousConnection;
import SMMPClient.Crypto.CryptoUtils;
import SMMPClient.Crypto.KeyringManager;
import SMMPClient.Exceptions.CryptoExceptions.SignatureVerificationException;
import SMMPClient.Exceptions.PkiExceptions.CertificateValidationException;
import SMMPClient.Exceptions.PkiExceptions.MissingCertificateException;
import SMMPClient.MessageFormats.MessageType;
import SMMPClient.MessageFormats.ProtocolMessage;
import SMMPClient.MessageFormats.SMMPMessage;
import SMMPClient.Connections.SMMPAnonConnection;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Instant;

/**
 * A SMMP specific implementation of the anonymous adapter interface provided by the agent.
 * This class is responsible for handling the messages received from the agent and passing them to the SMMP adapter
 * after they have been validated and verified.
 */
@Slf4j
public class AnonymousAdapterImpl implements AnonymousAdapter
{
    private final KeyringManager keyringManager;
    private final SMMPAnonAdapter adapter;


    /**
     * Creates a new instance of the anonymous adapter implementation
     *
     * @param keyringManager The keyring manager used to verify the signatures of the messages
     * @param adapter        The SMMP adapter that will receive the callbacks.
     */
    public AnonymousAdapterImpl(KeyringManager keyringManager, SMMPAnonAdapter adapter)
    {
        this.keyringManager = keyringManager;
        this.adapter = adapter;
    }


    /**
     * Called when the agent has successfully connected to the edge router in anonymous mode.
     * Wraps the connection in a SMMP specific connection and passes it to the SMMP adapter.
     *
     * @param connection The connection that was established.
     */
    @Override
    public void onConnect(AnonymousConnection connection)
    {
        adapter.onConnect(new SMMPAnonConnection(connection));
        log.info("Successfully connected to the anonymous network");
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
            ProtocolMessage protocolMessage = ProtocolMessage.parseFrom(message);

            if (protocolMessage.getType() == MessageType.MESSAGE)
            {
                processMessage(protocolMessage, sender, messageId, subject, expires);
            }
            else
            {
                log.error("Received an unknown message type");
            }
        }
        catch (InvalidProtocolBufferException | MissingCertificateException | CertificateValidationException | CertificateException | SignatureVerificationException | KeyStoreException e)
        {
            log.error("Error while processing message", e);
        }
    }


    /**
     * Called when the agent has disconnected from the edge router.
     *
     * @param statusCode The status code of the disconnection
     * @param reason     The reason for the disconnection
     */
    @Override
    public final void onDisconnect(int statusCode, String reason)
    {
        adapter.onDisconnect(statusCode, reason);
        log.info("Disconnected from the anonymous connection with status code: {} and reason: {}", statusCode, reason);
    }


    /**
     * Called when the agent has encountered an error while connecting to the edge router.
     *
     * @param ex The throwable that was thrown
     */
    @Override
    public final void onConnectionError(Throwable ex)
    {
        adapter.onConnectionError(ex);
        log.error("Error while connecting to the anonymous connection", ex);
    }


    /**
     * Processes a SMMP message by verifying the signature and passing it to the SMMP adapter if everything is valid.
     *
     * @param protocolMessage The message to process
     * @param sender          The sender of the message
     * @param messageId       The ID of the message
     * @param subject         The subject of the message
     * @param expires         The expiration time of the message
     * @throws InvalidProtocolBufferException If the message is not a valid protocol message
     * @throws CertificateException           If the certificate is not a valid X.509 certificate
     * @throws KeyStoreException              If the key store is not initialized
     * @throws MissingCertificateException    If the certificate is not found in the key store
     * @throws CertificateValidationException If the certificate is not valid
     * @throws SignatureVerificationException If the signature is not valid
     */
    private void processMessage(ProtocolMessage protocolMessage, String sender, String messageId, String subject, Instant expires) throws InvalidProtocolBufferException, CertificateException, KeyStoreException, MissingCertificateException, CertificateValidationException, SignatureVerificationException
    {
        SMMPMessage smmpMessage = SMMPMessage.parseFrom(protocolMessage.getContent());
        byte[] signature = smmpMessage.getSignature().toByteArray();
        byte[] content = smmpMessage.getPayload().toByteArray();
        X509Certificate certificate = getCertificate(smmpMessage.getCertificate().toByteArray());

        if (smmpMessage.getIsEncrypted())
        {
            log.warn("Received an encrypted message, unable to process");
            return;
        }

        if (!verifyCertificate(certificate, sender))
        {
            log.warn("Received a message from an untrusted sender: {}", sender);
            return;
        }

        if (CryptoUtils.verifySignature(keyringManager.getPublicKey(sender), content, signature))
        {
            adapter.onSubjectCastMessage(messageId, sender, subject, expires, content);
            log.info("Successfully verified the signature of the message from sender: {}", sender);
        }
        else
        {
            log.error("Signature verification failed for message from sender: {}", sender);
        }
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