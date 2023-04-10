package SMMPClient.Exceptions;

public class DecryptionException extends CryptoOperationException
{
    public DecryptionException(String message)
    {
        super(message);
    }

    public DecryptionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DecryptionException(Throwable cause)
    {
        super(cause);
    }
}
