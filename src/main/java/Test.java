import Agent.Agent.AgentConnectionHandlerFactory;
import Agent.Agent.IAgentConnectionHandler;
import Agent.Exceptions.AgentConnectionHandlerInitException;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Test
{
    public static void main(String[] args) throws AgentConnectionHandlerInitException, InterruptedException
    {
        IAgentConnectionHandler agentConnectionHandler = AgentConnectionHandlerFactory.create();

        String keystorePath = "C:\\\\Users\\\\Viljar\\\\IdeaProjects\\\\MMS-Agent\\\\src\\\\main\\\\resources\\\\truststore-root-ca.p12";
        String keystorePassword = "changeit";
        TLSConfiguration tlsConfiguration = new TLSConfiguration(keystorePath, keystorePassword);
        TestImpl testImpl = new TestImpl();
        agentConnectionHandler.discover(new IDiscoveryListener()
        {
            @Override
            public void onDiscovered(List<RouterInfo> routerInfos)
            {
                for (RouterInfo routerInfo : routerInfos)
                {
                    System.out.println("Found router:" + routerInfo);
                    agentConnectionHandler.connectAnonymously(routerInfo, tlsConfiguration, testImpl);
                }
            }
        });

        Thread.sleep(10000);
    }
}
