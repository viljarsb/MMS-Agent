package Agent.Exceptions;

public class AgentConnectionHandlerInitException extends Exception
{
    public AgentConnectionHandlerInitException(String message)
    {
        super(message);
    }

    public AgentConnectionHandlerInitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public AgentConnectionHandlerInitException(Throwable cause)
    {
        super(cause);
    }
}
