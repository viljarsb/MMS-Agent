package SMMPClient.Exceptions.PkiExceptions;

public class CertificateValidationException extends Exception
{
    public CertificateValidationException(String message)
    {
        super(message);
    }

    public CertificateValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CertificateValidationException(Throwable cause)
    {
        super(cause);
    }
}
