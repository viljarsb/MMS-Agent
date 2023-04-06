import Agent.Agent.AnonymousAdapter;
import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AnonymousConnection;
import Agent.Connections.AuthenticatedConnection;

import java.time.Instant;
import java.util.List;

public class TestImpl implements AuthenticatedAdapter
{
    @Override
    public void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message)
    {
        System.out.println("onSubjectCastMessage");
    }


    @Override
    public void onDisconnect(int statusCode, String reason)
    {
        System.out.println("onDisconnect");
    }


    @Override
    public void onConnectionError(Throwable ex)
    {
        System.out.println("onConnectionError");
        System.out.println(ex.getMessage());
    }


    @Override
    public void onConnect(AuthenticatedConnection connection)
    {
        System.out.println("onConnect");
    }


    @Override
    public void onDirectMessage(String messageId, List<String> destinations, String sender, Instant expires, byte[] message)
    {
        System.out.println("onDirectMessage");
    }
}
