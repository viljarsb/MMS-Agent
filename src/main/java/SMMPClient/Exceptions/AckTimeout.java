package SMMPClient.Exceptions;

public class AckTimeout extends Exception
{
    public AckTimeout(String message)
    {
        super(message);
    }

    public AckTimeout(String message, Throwable cause)
    {
        super(message, cause);
    }

    public AckTimeout(Throwable cause)
    {
        super(cause);
    }
}
