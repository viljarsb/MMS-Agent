package Agent.Subscriptions;


import Agent.Misc.SubjectValidator;
import Protocols.MMTP.MessageFormats.MessageType;
import Protocols.MMTP.MessageFormats.ProtocolMessage;
import Protocols.MMTP.MessageFormats.Register;
import Protocols.MMTP.MessageFormats.Unregister;
import com.google.protobuf.ByteString;
import lombok.NonNull;
import org.eclipse.jetty.websocket.api.Session;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


/**
 * This class provides an implementation of the {@link ISubjectSubscriptionHandler} interface for managing subscriptions
 * to subjects for agents that are connected to some edge router.
 */
public class SubjectSubscriptionHandler implements ISubjectSubscriptionHandler
{
    private final Set<String> subscriptions = new ConcurrentSkipListSet<>();
    protected final Executor executor;
    protected final Session session;


    /**
     * Constructs a new instance of the {@link SubjectSubscriptionHandler} class with the given session and executor.
     *
     * @param session  the session to use for subscribing and unsubscribing
     * @param executor the executor to use for running subscription and unsubscription tasks
     */
    public SubjectSubscriptionHandler(@NonNull Session session, @NonNull Executor executor)
    {
        this.session = session;
        this.executor = executor;
    }


    /**
     * Subscribes to the given list of subjects and notifies the given listener when the operation is complete.
     * <p>
     * This method performs the subscription operation asynchronously using the executor provided in the constructor.
     * The listener's {@link ISubscribeListener#onSuccess()} method is called if the operation is successful, and its
     * {@link ISubscribeListener#onFailure(Throwable)} method is called if an exception occurs.
     *
     * @param subjects the subjects to subscribe to
     * @param listener the listener to notify when the operation is complete
     */
    @Override
    public void subscribeToSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener)
    {
        executor.execute(() ->
        {
            try
            {
                List<String> newSubjects = subjects.stream().filter(SubjectValidator::validate).filter(subscriptions::add).collect(Collectors.toList());
                if (!newSubjects.isEmpty())
                {
                    ProtocolMessage message = buildProtocolMessage(MessageType.REGISTER, Register.newBuilder().addAllInterests(newSubjects).build().toByteString());
                    session.getRemote().sendBytes(message.toByteString().asReadOnlyByteBuffer());
                }
                listener.onSuccess();
            }
            catch (Exception e)
            {
                listener.onFailure(e);
            }
        });
    }


    /**
     * Subscribes to the given list of subjects and returns a {@link CompletableFuture} that completes when the
     * operation is complete.
     * <p>
     * This method performs the subscription operation asynchronously using the executor provided in the constructor.
     * The future completes successfully if the operation is successful, and completes exceptionally with an
     * exception if an exception occurs.
     *
     * @param subjects the subjects to subscribe to
     * @return a CompletableFuture that completes when the operation is complete
     */
    @Override
    public CompletableFuture<Void> subscribeToSubjects(@NonNull List<String> subjects)
    {
        CompletableFuture<Void> future = new CompletableFuture<>();
        subscribeToSubjects(subjects, new ISubscribeListener()
        {
            @Override
            public void onSuccess()
            {
                future.complete(null);
            }


            @Override
            public void onFailure(Throwable e)
            {
                future.completeExceptionally(e);
            }
        });
        return future;
    }


    /**
     * Unsubscribes from the given list of subjects and notifies the given listener when the operation is complete.
     * <p>
     * This method performs the unsubscription operation asynchronously using the executor provided in the constructor.
     * The listener's {@link ISubscribeListener#onSuccess()} method is called if the operation is successful, and its
     * {@link ISubscribeListener#onFailure(Throwable)} method is called if an exception occurs.
     *
     * @param subjects the subjects to unsubscribe from
     * @param listener the listener to notify when the operation is complete
     */
    @Override
    public void unsubscribeFromSubjects(@NonNull List<String> subjects, @NonNull ISubscribeListener listener)
    {
        executor.execute(() ->
        {
            try
            {
                List<String> removedSubjects = subjects.stream().filter(subscriptions::remove).collect(Collectors.toList());
                if (!removedSubjects.isEmpty())
                {
                    session.getRemote().sendBytes(buildProtocolMessage(MessageType.UNREGISTER, Unregister.newBuilder().addAllInterests(removedSubjects).build().toByteString()).toByteString().asReadOnlyByteBuffer());
                }
                listener.onSuccess();
            }
            catch (Exception e)
            {
                listener.onFailure(e);
            }
        });
    }


    /**
     * Unsubscribes from the given list of subjects and returns a {@link CompletableFuture} that completes when the
     * operation is complete.
     * <p>
     * This method performs the unsubscription operation asynchronously using the executor provided in the constructor.
     * The future completes successfully if the operation is successful, and completes exceptionally with an
     * exception if an exception occurs.
     *
     * @param subjects the subjects to unsubscribe from
     * @return a CompletableFuture that completes when the operation is complete
     */
    @Override
    public CompletableFuture<Void> unsubscribeFromSubjects(@NonNull List<String> subjects)
    {
        CompletableFuture<Void> future = new CompletableFuture<>();
        unsubscribeFromSubjects(subjects, new ISubscribeListener()
        {
            @Override
            public void onSuccess()
            {
                future.complete(null);
            }


            @Override
            public void onFailure(Throwable e)
            {
                future.completeExceptionally(e);
            }
        });
        return future;
    }


    /**
     * Returns a copy of the current subscriptions.
     *
     * @return a list of the current subscriptions
     */
    @Override
    public List<String> getSubscriptions()
    {
        return List.copyOf(subscriptions);
    }


    /**
     * Checks whether the given subject is currently subscribed.
     *
     * @param subject the subject to check
     * @return true if the subject is subscribed, false otherwise
     */
    @Override
    public boolean isSubscribed(@NonNull String subject)
    {
        return subscriptions.contains(subject);
    }


    /**
     * Clears all current subscriptions.
     */
    @Override
    public void clear()
    {
        this.subscriptions.clear();
    }


    /**
     * Builds a {@link ProtocolMessage} with the given message type and content.
     *
     * @param messageType the type of message to build
     * @param content     the content of the message
     * @return a ProtocolMessage with the given message type and content
     */
    protected ProtocolMessage buildProtocolMessage(@NonNull MessageType messageType, @NonNull ByteString content)
    {
        return ProtocolMessage.newBuilder().setType(messageType).setContent(content).build();
    }
}

