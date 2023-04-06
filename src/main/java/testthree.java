import Agent.Agent.AnonymousAdapter;
import Agent.Connections.AnonymousConnection;

import java.time.Instant;

public class testthree implements AnonymousAdapter
{
    @Override
    public void onConnect(AnonymousConnection connection)
    {
        System.out.println("three: onConnect");
    }


    @Override
    public void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message)
    {

    }


    @Override
    public void onDisconnect(int statusCode, String reason)
    {

    }


    @Override
    public void onConnectionError(Throwable ex)
    {
        System.out.println("three: onConnectionError");
        System.out.println(ex.getMessage());
    }
}
