package SMMPClient.Exceptions;

public class MissingCertificateException extends Exception
{
    public MissingCertificateException(String message)
    {
        super(message);
    }

    public MissingCertificateException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MissingCertificateException(Throwable cause)
    {
        super(cause);
    }
}
