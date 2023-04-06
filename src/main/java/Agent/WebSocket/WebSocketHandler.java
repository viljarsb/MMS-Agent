package Agent.WebSocket;

import Agent.Agent.AgentAdapter;

import Agent.Exceptions.ConnectException;
import Agent.MessageHandler.*;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import java.util.concurrent.Executor;

@Slf4j
public class WebSocketHandler extends WebSocketAdapter
{
    private final IConnectionListener connectionListener;
    private final DisconnectionHook disconnectionHook;
    private final MessageHandler messageHandler;
    private final Executor executor;
    private Session session;
    private String ipAddress;

    public WebSocketHandler(@NonNull AgentAdapter listener, @NonNull DisconnectionHook disconnectionHook, @NonNull Executor executor)
    {
        this.connectionListener = (IConnectionListener) listener;
        this.disconnectionHook = disconnectionHook;
        this.messageHandler = MessageHandlerFactory.createMessageHandler((SubjectMessageListener) listener, executor);
        this.executor = executor;
    }


    @Override
    public void onWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
        executor.execute(() -> handleWebSocketConnect(session));
    }


    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len)
    {
        super.onWebSocketBinary(payload, offset, len);
        executor.execute(() -> handleMessage(payload, offset, len));
    }


    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        executor.execute(() -> handleTextMessage(message));
    }


    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode, reason);
        executor.execute(() -> handleWebSocketClose(statusCode, reason));
    }


    @Override
    public void onWebSocketError(Throwable error)
    {
        super.onWebSocketError(error);
        executor.execute(() -> handleWebSocketError(error));
    }


    private void handleWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
        this.session = session;
        ipAddress = session.getRemoteAddress().getAddress().getHostAddress();
        log.info("WebSocket connection established with IP address {}", getIPAddress());
    }



    private void handleMessage(byte[] payload, int offset, int len)
    {
        super.onWebSocketBinary(payload, offset, len);
        String ipAddress = getIPAddress();
        log.debug("Received binary WebSocket message with length {} from IP address {}", len, ipAddress);
        messageHandler.handleMessage(payload, offset, len);
    }


    private void handleTextMessage(String message)
    {
        super.onWebSocketText(message);
        String ipAddress = getIPAddress();
        log.warn("Received text WebSocket message from IP address " + ipAddress + ", which is not supported. Closing the connection.");
        this.getSession().close(StatusCode.BAD_DATA, "Edge Router sent text data, only binary supported.");
    }


    private void handleWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode, reason);
        String ipAddress = getIPAddress();
        disconnectionHook.onDisconnect(getSession());
        connectionListener.onDisconnect(statusCode, reason);
        log.info("WebSocket connection closed with status code {} and reason '{}' from IP address {}", statusCode, reason, ipAddress);
    }


    private void handleWebSocketError(Throwable error)
    {
        if (getSession() == null)
        {
            handleWebSocketConnectionError(error);
        }

        else
        {
            handleWebSocketCommunicationError(error);
        }
    }


    private void handleWebSocketCommunicationError(Throwable error)
    {
        String ipAddress = getIPAddress();
        log.error("WebSocket communication error from IP address: {} ", ipAddress, error);
        super.onWebSocketError(error);
    }


    private void handleWebSocketConnectionError(Throwable error)
    {
        ConnectException ex = new ConnectException(error.getMessage(), error.getCause());
        log.error("WebSocket connection error", ex);
        connectionListener.onConnectionError(ex);
    }


    private String getIPAddress()
    {
        return ipAddress;
    }

    @Override
    public Session getSession()
    {
        return session;
    }

}

