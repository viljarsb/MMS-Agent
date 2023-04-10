package SMMPClient.Acks;

import Agent.Connections.AuthenticatedConnection;
import Agent.Exceptions.ConnectException;
import SMMPClient.Acks.AckEntry.*;
import SMMPClient.Acks.AckEntry.DefaultEntryHandlers.DefaultMultiDestCompletionHandler;
import SMMPClient.Acks.AckEntry.DefaultEntryHandlers.DefaultSingleDestCompletionHandler;
import SMMPClient.Acks.Handlers.MultiDeliveryCompletionHandler;
import SMMPClient.Acks.Handlers.SingleDeliveryCompletionHandler;
import SMMPClient.Acks.ResultObjects.MultiDestDeliveryResult;
import SMMPClient.Acks.ResultObjects.SingleDestDeliveryResult;
import SMMPClient.Crypto.CryptoUtils;
import SMMPClient.Exceptions.SignatureGenerationException;
import SMMPClient.MessageFormats.MessageType;
import SMMPClient.MessageFormats.SMMPUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * AckTracker is responsible for tracking acknowledgements for sent messages and for sending acks.
 */
@Slf4j
public class AckTracker implements IAckTracker
{
    private static final int INITIAL_DELAY = 5;
    private static final int MAX_DELAY = 600;
    private static final int DELAY_MULTIPLIER = 2;
    private static final TimeUnit DELAY_UNIT = TimeUnit.SECONDS;
    private static final int MAX_THREADS = 3;

    private final ScheduledExecutorService scheduledWorkerPool = Executors.newScheduledThreadPool(MAX_THREADS);
    private final Map<String, AckEntry<?>> waitingAcknowledgements = new ConcurrentHashMap<>();

    private final AuthenticatedConnection connection;
    private final ECPrivateKey privateKey;
    private final X509Certificate certificate;


    /**
     * Constructs a new {@link AckTracker} instance.
     */
    public AckTracker(AuthenticatedConnection connection, X509Certificate myCertificate, ECPrivateKey myPrivateKey)
    {
        this.connection = connection;
        this.privateKey = myPrivateKey;
        this.certificate = myCertificate;
    }


    /**
     * Waits for an acknowledgement from a single destination using a handler.
     *
     * @param messageId   the id of the message to require an ack for
     * @param destination the destination that should ack this message
     * @param message     the message to be resent if no ack is received before a timeout
     * @param handler     a {@link SingleDeliveryCompletionHandler} instance to handle the delivery result
     */
    @Override
    public void waitForSingleDestAck(@NonNull String messageId, @NonNull String destination, @NonNull byte[] message, Instant expires, @NonNull SingleDeliveryCompletionHandler handler)
    {
        SingleDestAckEntry entry = new SingleDestAckEntry(messageId, destination, message, expires, handler);
        waitingAcknowledgements.put(messageId, entry);
        scheduledWorkerPool.schedule(() -> checkAndRetransmit(entry, INITIAL_DELAY, MAX_DELAY), INITIAL_DELAY, DELAY_UNIT);
        log.debug("Waiting for ack for message {} to destination {}", messageId, destination);
    }


    /**
     * Waits for an acknowledgement from a single destination and returns a CompletableFuture with a delivery result.
     *
     * @param messageId   the id of the message to require an ack for
     * @param destination the destination that should ack this message
     * @param message     the message to be resent if no ack is received before a timeout.
     * @param expires     the time at which the message expires and should no longer be resent
     * @return a {@link CompletableFuture} containing a {@link SingleDestDeliveryResult} instance
     */
    @Override
    public CompletableFuture<SingleDestDeliveryResult> waitForSingleDestAck(@NonNull String messageId, @NonNull String destination, @NonNull byte[] message, Instant expires)
    {
        CompletableFuture<SingleDestDeliveryResult> future = new CompletableFuture<>();
        DefaultSingleDestCompletionHandler handler = new DefaultSingleDestCompletionHandler(future);

        SingleDestAckEntry entry = new SingleDestAckEntry(messageId, destination, message, expires, handler);
        waitingAcknowledgements.put(messageId, entry);
        scheduledWorkerPool.schedule(() -> checkAndRetransmit(entry, 5, 600), 5, TimeUnit.SECONDS);
        log.debug("Waiting for ack for message {} to destination {}", messageId, destination);
        return future;
    }


    /**
     * Waits for acknowledgements from multiple destinations using a handler.
     *
     * @param messageId    the id of the message to require an ack for
     * @param destinations a list of destinations that should ack this message
     * @param message      the message to be resent if no ack is received before a timeout.
     * @param expires      the time at which the message expires and should no longer be resent
     * @param handler      a {@link MultiDeliveryCompletionHandler} instance to handle the delivery result
     */
    @Override
    public void waitForMultiDestAck(@NonNull String messageId, @NonNull List<String> destinations, @NonNull byte[] message, Instant expires, @NonNull MultiDeliveryCompletionHandler handler)
    {
        MultiDestAckEntry entry = new MultiDestAckEntry(messageId, destinations, message, expires, handler);
        waitingAcknowledgements.put(messageId, entry);
        scheduledWorkerPool.schedule(() -> checkAndRetransmit(entry, 5, 600), 5, TimeUnit.SECONDS);
        log.debug("Waiting for ack for message {} to destinations {}", messageId, destinations);
    }


    /**
     * Waits for acknowledgements from multiple destinations and returns a CompletableFuture with a delivery result.
     *
     * @param messageId    the id of the message to require an ack for
     * @param destinations a list of destinations that should ack this message
     * @param message      the message to be resent if no ack is received before a timeout.
     * @param expires      the time at which the message expires and should no longer be resent
     * @return a {@link CompletableFuture} containing a {@link MultiDestDeliveryResult} instance
     */
    @Override
    public CompletableFuture<MultiDestDeliveryResult> waitForMultiDestAck(@NonNull String messageId, @NonNull List<String> destinations, @NonNull byte[] message, Instant expires)
    {
        CompletableFuture<MultiDestDeliveryResult> future = new CompletableFuture<>();
        DefaultMultiDestCompletionHandler handler = new DefaultMultiDestCompletionHandler(future);

        MultiDestAckEntry entry = new MultiDestAckEntry(messageId, destinations, message, expires, handler);
        waitingAcknowledgements.put(messageId, entry);
        scheduledWorkerPool.schedule(() -> checkAndRetransmit(entry, 5, 600), 5, TimeUnit.SECONDS);
        log.debug("Waiting for ack for message {} to destinations {}", messageId, destinations);
        return future;
    }


    /**
     * Processes an acknowledgement received from a destination.
     *
     * @param messageId   the id of the message to acknowledged
     * @param destination the destination that acknowledged the message
     */
    @Override
    public void acknowledge(@NonNull String messageId, @NonNull String destination)
    {
        AckEntry<?> entry = waitingAcknowledgements.get(messageId);

        if (entry != null)
        {
            if (entry.acknowledge(destination))
            {
                log.debug("Ack received for message {} to destination {}", messageId, destination);
                if (entry.isFullyAcknowledged())
                {
                    log.debug("All acks received for message {}", messageId);
                    waitingAcknowledgements.remove(messageId);
                }
            }
        }
    }


    /**
     * Sends an acknowledgement to a destination for a specific message.
     *
     * @param messageId   the id of the acknowledged message
     * @param destination the destination to send the acknowledgement to
     */
    @Override
    public void sendAck(@NonNull String messageId, @NonNull String destination)
    {
        try
        {
            log.debug("Sending ack for message {} to destination {}", messageId, destination);
            byte[] signature = CryptoUtils.sign(privateKey, messageId.getBytes());
            byte[] ackBytes = SMMPUtils.createAck(messageId, signature, certificate.getEncoded()).toByteArray();
            byte[] protocolBytes = SMMPUtils.createProtocolMessage(ackBytes, MessageType.ACK).toByteArray();
            connection.sendDirect(destination, protocolBytes, null);
        }

        catch (SignatureGenerationException | ConnectException | CertificateEncodingException e)
        {
            log.error("Failed to send ack.");
        }
    }


    /**
     * Retransmits a message to a list of unacknowledged destinations.
     *
     * @param unacknowledgedDestinations a list of destinations that have not acknowledged the message
     * @param message                    the message to be resent
     * @param messageId                  the id of the message
     * @param expires                    the time at which the message expires and should no longer be resent
     */
    private void retransmit(@NonNull List<String> unacknowledgedDestinations, @NonNull String messageId, @NonNull byte[] message, Instant expires)
    {
        try
        {
            log.debug("Retransmitting message {} to destinations {}", messageId, unacknowledgedDestinations);
            connection.sendDirect(unacknowledgedDestinations, message, null);
            log.debug("Retransmitted message {} to destinations {}", messageId, unacknowledgedDestinations);
        }

        catch (ConnectException ex)
        {
            log.error("Failed to retransmit message {} to destinations {}.", messageId, unacknowledgedDestinations, ex);
        }
    }


    /**
     * Checks the status of an {@link AckEntry} and retransmits the message if necessary.
     *
     * @param entry    the {@link AckEntry} instance to check
     * @param delay    the initial delay before retransmitting the message
     * @param maxDelay the maximum delay allowed for retransmitting the message
     */
    private void checkAndRetransmit(AckEntry<?> entry, int delay, int maxDelay)
    {
        if (entry.isFullyAcknowledged())
        {
            log.debug("All acks received for message {}", entry.getMessageId());
            return;
        }

        if (delay < maxDelay && entry.getExpires().isAfter(Instant.now()))
        {
            log.debug("No ack received for message {} after {} seconds, retransmitting", entry.getMessageId(), delay);
            List<String> unacknowledgedDestinations = entry.getUnacknowledgedDestinations();
            byte[] message = entry.getMessage();
            String messageId = entry.getMessageId();
            Instant expires = entry.getExpires();
            retransmit(unacknowledgedDestinations, messageId, message, expires);
            scheduledWorkerPool.schedule(() -> checkAndRetransmit(entry, delay * DELAY_MULTIPLIER, maxDelay), delay, TimeUnit.SECONDS);
        }

        else
        {
            int numRetries = 0;
            int currentWaitTime = INITIAL_DELAY;

            while (currentWaitTime < MAX_DELAY)
            {
                currentWaitTime *= DELAY_MULTIPLIER;
                numRetries++;
            }

            int totalWaitTime = INITIAL_DELAY + (DELAY_MULTIPLIER * ((int) Math.pow(2, numRetries) - 1));
            int waited = Math.min(totalWaitTime, maxDelay);
            log.debug("No ack received for message {} after {} seconds, giving up", entry.getMessageId(), waited);
            waitingAcknowledgements.remove(entry.getMessageId());
            entry.timeout();
        }
    }
}