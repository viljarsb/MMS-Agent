import Agent.Agent.AnonymousAdapter;
import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AnonymousConnection;
import Agent.Connections.AuthenticatedConnection;
import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;
import SMMPClient.Adapters.SMMPAuthAdapter;
import SMMPClient.Connections.SMMPAuthConnection;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestImpl implements SMMPAuthAdapter
{

    public void sleepRandomSeconds()
    {
        int minSeconds = 1;
        int maxSeconds = 10;
        int randomSeconds = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);
        try
        {
            Thread.sleep(randomSeconds * 1000L);
        }

        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }


    @Override
    public void onConnect(SMMPAuthConnection connection)
    {
        System.out.println("Successfully connected to the authenticated network");
    }


    @Override
    public void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message)
    {

    }


    @Override
    public void onDirectMessage(String messageId, List<String> destinations, String sender, Instant expires, byte[] message)
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
