package Agent.Exceptions;

public class SendingException extends Exception
{
    public SendingException()
    {
        super("Error sending message");
    }

    public SendingException(String message)
    {
        super(message);
    }

    public SendingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
