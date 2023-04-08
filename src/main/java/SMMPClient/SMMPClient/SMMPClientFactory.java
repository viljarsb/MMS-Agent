package SMMPClient.SMMPClient;

import Agent.Agent.AgentConnectionHandler;
import Agent.Agent.AgentConnectionHandlerFactory;
import Agent.Exceptions.AgentConnectionHandlerInitException;


public class SMMPClientFactory
{
    public static SMMPConnectionHandler create(String address)
    {
        try
        {
            return new SMMPConnectionHandler((AgentConnectionHandler) AgentConnectionHandlerFactory.create(address));
        }

        catch (AgentConnectionHandlerInitException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static SMMPConnectionHandler create()
    {
        try
        {
            return new SMMPConnectionHandler((AgentConnectionHandler) AgentConnectionHandlerFactory.create());
        }

        catch (AgentConnectionHandlerInitException e)
        {
            throw new RuntimeException(e);
        }
    }
}
