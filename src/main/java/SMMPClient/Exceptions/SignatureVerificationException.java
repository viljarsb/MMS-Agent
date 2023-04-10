package SMMPClient.Exceptions;

public class SignatureVerificationException extends CryptoOperationException
{
    public SignatureVerificationException(String message)
    {
        super(message);
    }

    public SignatureVerificationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SignatureVerificationException(Throwable cause)
    {
        super(cause);
    }
}
