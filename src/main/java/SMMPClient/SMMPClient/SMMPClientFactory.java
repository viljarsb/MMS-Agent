package SMMPClient.SMMPClient;

import Agent.Agent.AgentConnectionHandler;
import Agent.Agent.AgentConnectionHandlerFactory;
import Agent.Exceptions.AgentConnectionHandlerInitException;
import SMMPClient.Exceptions.SMMPConnectionHandlerInitException;

/**
 * Wrapper around {@link AgentConnectionHandlerFactory}
 */
public class SMMPClientFactory
{
    public static SMMPConnectionHandler create(String address) throws SMMPConnectionHandlerInitException
    {
        try
        {
            return new SMMPConnectionHandler((AgentConnectionHandler) AgentConnectionHandlerFactory.create(address));
        }

        catch (AgentConnectionHandlerInitException e)
        {
            throw new SMMPConnectionHandlerInitException("Failed to init SMMP connection handler", e.getCause());
        }
    }


    public static SMMPConnectionHandler create() throws SMMPConnectionHandlerInitException
    {
        try
        {
            return new SMMPConnectionHandler((AgentConnectionHandler) AgentConnectionHandlerFactory.create());
        }

        catch (AgentConnectionHandlerInitException e)
        {
            throw new SMMPConnectionHandlerInitException("Failed to init SMMP connection handler", e.getCause());
        }
    }
}
