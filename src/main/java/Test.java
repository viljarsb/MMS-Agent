import Agent.Agent.AgentConnectionHandlerFactory;
import Agent.Agent.IAgentConnectionHandler;
import Agent.Exceptions.AgentConnectionHandlerInitException;
import Agent.Exceptions.MMSSecurityException;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class Test
{
    public static void main(String[] args) throws AgentConnectionHandlerInitException, InterruptedException
    {
        IAgentConnectionHandler agentConnectionHandler = AgentConnectionHandlerFactory.create();

        String truststore = "C:\\\\Users\\\\Viljar\\\\IdeaProjects\\\\MMS-Agent\\\\src\\\\main\\\\resources\\\\truststore-root-ca.p12";
        String trusstorePass = "changeit";

        String keystore = "C:\\\\Users\\\\Viljar\\\\IdeaProjects\\\\MMS-Agent\\\\src\\\\main\\\\resources\\\\keystore-test.p12";
        String keystorePass = "qm32eoc39omqurd546ggs7mldo";
        TLSConfiguration tlsConfiguration = new TLSConfiguration(truststore, trusstorePass);
        mTLSConfiguration mTLSConfiguration = new mTLSConfiguration(truststore, trusstorePass, keystore, keystorePass);
        TestImpl testImpl = new TestImpl();
        TestImplTwo testImplTwo = new TestImplTwo();


        agentConnectionHandler.discover(new IDiscoveryListener()
        {
            @Override
            public void onDiscovered(List<RouterInfo> routerInfos)
            {
                for (RouterInfo routerInfo : routerInfos)
                {
                    System.out.println("Found router:" + routerInfo);
                    agentConnectionHandler.connectAuthenticated(routerInfo, mTLSConfiguration, testImpl);
                    agentConnectionHandler.connectAnonymously(routerInfo, mTLSConfiguration, testImplTwo);
                }
            }
        });

        Thread.sleep(10000);
    }
}
