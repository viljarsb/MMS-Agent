package SMMPClient.Exceptions.CryptoExceptions;

public class EncryptionException extends CryptoOperationException
{
    public EncryptionException(String message)
    {
        super(message);
    }

    public EncryptionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EncryptionException(Throwable cause)
    {
        super(cause);
    }
}
