import Agent.Agent.AnonymousAdapter;
import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AnonymousConnection;
import Agent.Connections.AuthenticatedConnection;
import Agent.Exceptions.ConnectException;
import Agent.Subscriptions.ISubscribeListener;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestImpl implements AuthenticatedAdapter
{
    @Override
    public void onConnect(AuthenticatedConnection connection)
    {
        System.out.println("Program 1: connected successfully to: " + connection.getConnectionAddress());
        int counter = 0;

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


            try
            {
                sleepRandomSeconds();
                String message = "message number " + counter;
                connection.publish("test", message.getBytes(), Instant.now().plusSeconds(10));
                counter++;
            }

            catch (ConnectException e)
            {
               System.out.println("Program 1: failed to publish message");
            }
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
    public void onDirectMessage(String messageId, List<String> destinations, String sender, Instant expires, byte[] message)
    {
        System.out.println("onDirectMessage");
    }


    @Override
    public void onSubjectCastMessage(String messageId, String sender, String subject, Instant expires, byte[] message)
    {
        System.out.println("Program 1: received message: " + new String(message));
        System.out.println("Program 1: sender: " + sender);
        System.out.println("Program 1: subject: " + subject);
        System.out.println("Program 1: messageId: " + messageId);
        System.out.println("Program 1: message: " + new String(message));
    }


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
}
