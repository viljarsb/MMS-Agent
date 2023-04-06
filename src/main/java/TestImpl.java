import Agent.Agent.AnonymousAdapter;
import Agent.Connections.AnonymousConnection;

import java.time.Instant;

public class TestImpl implements AnonymousAdapter
{

    @Override
    public void onConnect(AnonymousConnection connection)
    {

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

    }
}
