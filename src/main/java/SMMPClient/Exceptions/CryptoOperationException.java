package SMMPClient.Exceptions;

public class CryptoOperationException extends Exception
{
    public CryptoOperationException(String message)
    {
        super(message);
    }

    public CryptoOperationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CryptoOperationException(Throwable cause)
    {
        super(cause);
    }
}
