package SMMPClient.Exceptions.CryptoExceptions;

public class SignatureGenerationException extends CryptoOperationException
{
    public SignatureGenerationException(String message)
    {
        super(message);
    }

    public SignatureGenerationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SignatureGenerationException(Throwable cause)
    {
        super(cause);
    }
}
