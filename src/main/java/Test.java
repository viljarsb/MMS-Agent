import Agent.Agent.AgentConnectionHandlerFactory;
import Agent.Agent.IAgentConnectionHandler;
import Agent.Exceptions.AgentConnectionHandlerInitException;
import Agent.Exceptions.MMSSecurityException;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;
import SMMPClient.Adapters.AuthenticatedAdapterImpl;
import SMMPClient.Adapters.SMMPAuthAdapter;
import SMMPClient.Connections.SMMPAuthConnection;
import SMMPClient.Crypto.KeyringManager;
import SMMPClient.SMMPClient.SMMPClientFactory;
import SMMPClient.SMMPClient.SMMPConnectionHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;


@Slf4j
public class Test
{
    public static void main(String[] args) throws AgentConnectionHandlerInitException, InterruptedException, MMSSecurityException
    {
        IAgentConnectionHandler agentConnectionHandler = AgentConnectionHandlerFactory.create();

        String truststore = "C:\\\\Users\\\\Viljar\\\\IdeaProjects\\\\MMS-Agent\\\\src\\\\main\\\\resources\\\\truststore-root-ca.p12";
        String trusstorePass = "changeit";

        String keystore = "C:\\\\Users\\\\Viljar\\\\IdeaProjects\\\\MMS-Agent\\\\src\\\\main\\\\resources\\\\keystore-test.p12";
        String keystorePass = "qm32eoc39omqurd546ggs7mldo";
        TLSConfiguration tlsConfiguration = new TLSConfiguration(truststore, trusstorePass);
        mTLSConfiguration mTLSConfiguration = new mTLSConfiguration(truststore, trusstorePass, keystore, keystorePass);

        KeyringManager keyringManager = KeyringManager.create(keystore, keystorePass, truststore, trusstorePass, "null", "null");
        SMMPConnectionHandler connectionHandler = SMMPClientFactory.create();
        connectionHandler.discover().thenAccept(routers -> {
            for (RouterInfo router : routers)
            {
                System.out.println(router);
                connectionHandler.connectAuthenticated(router, keyringManager, new SMMPAuthAdapter()
                {
                    @Override
                    public void onConnect(SMMPAuthConnection connection)
                    {
                        System.out.println("Connected to ");
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
                        System.out.println(ex.getMessage() + " " + ex.getCause());
                    }
                });
            }
        });

        Thread.sleep(10000);
    }
}
