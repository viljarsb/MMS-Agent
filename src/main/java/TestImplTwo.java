import Agent.Agent.AnonymousAdapter;
import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AnonymousConnection;
import Agent.Connections.AuthenticatedConnection;
import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;

import java.time.Instant;
import java.util.List;

public class TestImplTwo implements AnonymousAdapter
{
    @Override
    public void onConnect(AnonymousConnection connection)
    {
        try
        {
            connection.subscribeToSubject("test", new ISubscribeListener()
            {
                @Override
                public void onSuccess()
                {
                    System.out.println("two: subscribe success");
                }


                @Override
                public void onFailure(Throwable ex)
                {
                    System.out.println("two: subscribe failure");
                    System.out.println(ex.getMessage());
                }
            });
        }
        catch (ConnectException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message)
    {
        System.out.println("Program 2: received message: " + new String(message));
        System.out.println("Program 2: sender: " + sender);
        System.out.println("Program 2: subject: " + subject);
        System.out.println("Program 2: messageId: " + messageId);
        System.out.println("Program 2: message: " + new String(message));
    }


    @Override
    public void onDisconnect(int statusCode, String reason)
    {

    }


    @Override
    public void onConnectionError(Throwable ex)
    {

    }
}
