package SMMPClient.SMMP;

import Agent.Agent.AuthenticatedAdapter;
import Agent.Connections.AuthenticatedConnection;

import java.time.Instant;
import java.util.List;

public class SMMPAgentAdapter implements AuthenticatedAdapter
{
    @Override
    public void onConnect(AuthenticatedConnection connection)
    {

    }


    @Override
    public void onDirectMessage(String messageId, List<String> destinations, String sender, Instant expires, byte[] message)
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
