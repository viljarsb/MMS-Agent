package Agent.Subscriptions;

import Protocols.MMTP.MessageFormats.MessageType;
import Protocols.MMTP.MessageFormats.Register;
import Protocols.MMTP.MessageFormats.Unregister;
import lombok.NonNull;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A subscription handler that manages subscriptions for direct messages (DMs).
 * <p>
 * This class extends {@link SubjectSubscriptionHandler} and provides additional methods for managing subscriptions to DMs.
 */
public class DmSubscriptionHandler extends SubjectSubscriptionHandler implements IDmSubscriptionHandler
{
    private final AtomicBoolean subscribedToDM = new AtomicBoolean(false);

    /**
     * Constructs a new DmSubscriptionHandler with the given session and executor.
     *
     * @param session  the session to use for subscriptions
     * @param executor the executor to use for performing subscription operations
     */
    public DmSubscriptionHandler(@NonNull Session session, @NonNull Executor executor)
    {
        super(session, executor);
    }

    /**
     * Subscribes to direct messages and notifies the given listener when the operation is complete.
     * <p>
     * This method performs the subscription operation asynchronously using the executor provided in the constructor.
     * The listener's {@link ISubscribeListener#onSuccess()} method is called if the operation is successful, and its
     * {@link ISubscribeListener#onFailure(Throwable)} method is called if an exception occurs.
     *
     * @param listener the listener to notify when the operation is complete
     */
    @Override
    public void subscribeToDM(@NonNull ISubscribeListener listener)
    {
        executor.execute(() ->
        {
            try
            {
                if (subscribedToDM.compareAndSet(false, true))
                {
                    session.getRemote().sendBytes(buildProtocolMessage(MessageType.REGISTER, Register.newBuilder().setWantDirectMessages(true).build().toByteString()).toByteString().asReadOnlyByteBuffer());
                }
                listener.onSuccess();
            }
            catch (Exception e)
            {
                subscribedToDM.set(false);
                listener.onFailure(e);
            }
        });
    }

    /**
     * Subscribes to direct messages and returns a {@link CompletableFuture} that completes when the operation is complete.
     * <p>
     * This method performs the subscription operation asynchronously using the executor provided in the constructor.
     * The future completes successfully if the operation is successful, and completes exceptionally with an exception
     * if an exception occurs.
     *
     * @return a CompletableFuture that completes when the operation is complete
     */
    @Override
    public CompletableFuture<Void> subscribeToDM()
    {
        CompletableFuture<Void> future = new CompletableFuture<>();
        subscribeToDM(new ISubscribeListener()
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
     * Unsubscribes from direct messages and notifies the given listener when the operation is complete.
     * <p>
     * This method performs the unsubscription operation asynchronously using the executor provided in the constructor.
     * The listener's {@link ISubscribeListener#onSuccess()} method is called if the operation is successful, and its
     * {@link ISubscribeListener#onFailure(Throwable)} method is called if an exception occurs.
     *
     * @param listener the listener to notify when the operation is complete
     */
    @Override
    public void unsubscribeFromDM(@NonNull ISubscribeListener listener)
    {
        executor.execute(() ->
        {
            try
            {
                if (subscribedToDM.compareAndSet(true, false))
                {
                    session.getRemote().sendBytes(buildProtocolMessage(MessageType.UNREGISTER, Unregister.newBuilder().setWantDirectMessages(true).build().toByteString()).toByteString().asReadOnlyByteBuffer());
                }
                listener.onSuccess();
            }
            catch (Exception e)
            {
                subscribedToDM.set(true);
                listener.onFailure(e);
            }
        });
    }


    /**
     * Unsubscribes from direct messages and returns a {@link CompletableFuture} that completes when the operation is complete.
     * <p>
     * This method performs the unsubscription operation asynchronously using the executor provided in the constructor.
     * The future completes successfully if the operation is successful, and completes exceptionally with an exception
     * if an exception occurs.
     *
     * @return a CompletableFuture that completes when the operation is complete
     */
    @Override
    public CompletableFuture<Void> unsubscribeFromDM()
    {
        CompletableFuture<Void> future = new CompletableFuture<>();
        unsubscribeFromDM(new ISubscribeListener()
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
     * Returns true if this subscription handler is subscribed to direct messages, false otherwise.
     *
     * @return true if this subscription handler is subscribed to direct messages, false otherwise
     */
    @Override
    public boolean isSubscribedToDM()
    {
        return subscribedToDM.get();
    }
}
