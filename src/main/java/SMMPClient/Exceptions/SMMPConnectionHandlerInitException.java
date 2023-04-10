package SMMPClient.Exceptions;

public class SMMPConnectionHandlerInitException extends Exception
{

    public SMMPConnectionHandlerInitException(String message)
    {
        super(message);
    }


    public SMMPConnectionHandlerInitException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SMMPConnectionHandlerInitException(Throwable cause)
    {
        super(cause);
    }
}
