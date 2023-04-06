package Agent.Exceptions;

public class NotConnectedException extends Exception
{
    public NotConnectedException()
    {
        super("AgentConnectionHandler is not connected");
    }

    public NotConnectedException(String message)
    {
        super(message);
    }

    public NotConnectedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
