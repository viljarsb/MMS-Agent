package Agent.Exceptions;

public class NoConnectionException extends Exception
{
    public NoConnectionException(String message)
    {
        super(message);
    }


    public NoConnectionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public NoConnectionException(Throwable cause)
    {
        super(cause);
    }
}
