import Agent.Agent.AgentConnectionHandlerFactory;
import Agent.Agent.IAgentConnectionHandler;
import Agent.Exceptions.AgentConnectionHandlerInitException;
import Agent.Exceptions.ConnectException;
import Agent.Exceptions.MMSSecurityException;
import Agent.ServiceDiscovery.IDiscoveryListener;
import Agent.ServiceDiscovery.RouterInfo;
import Agent.TLSConfiguration.TLSConfiguration;
import Agent.TLSConfiguration.mTLSConfiguration;
import SMMPClient.Acks.Handlers.SMMPSendingHandler;
import SMMPClient.Acks.Handlers.SingleDeliveryCompletionHandler;
import SMMPClient.Adapters.AuthenticatedAdapterImpl;
import SMMPClient.Adapters.SMMPAuthAdapter;
import SMMPClient.Connections.ISMMPAuthConnection;
import SMMPClient.Connections.SMMPAuthConnection;
import SMMPClient.Crypto.KeyringManager;
import SMMPClient.Exceptions.SMMPConnectionHandlerInitException;
import SMMPClient.SMMPClient.SMMPClientFactory;
import SMMPClient.SMMPClient.SMMPConnectionHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;


@Slf4j
public class Test
{
    public static void main(String[] args) throws AgentConnectionHandlerInitException, InterruptedException, MMSSecurityException, SMMPConnectionHandlerInitException
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
                    public void onConnect(ISMMPAuthConnection connection)
                    {
                        System.out.println("connected..");
                        try
                        {
                            connection.publish("heu", "hei".getBytes(), null, new SMMPSendingHandler()
                            {
                                @Override
                                public void onSuccess()
                                {
                                    System.out.println("sent");
                                }


                                @Override
                                public void onFailure(Throwable ex)
                                {
                                    System.out.println("failed to send.");
                                }
                            });


                            connection.sendDirectWithAcknowledgement("viljar", "viljar".getBytes(), null, true, new SingleDeliveryCompletionHandler()
                            {
                                @Override
                                public void onAcked(String destination)
                                {
                                    System.out.println("acked");
                                }


                                @Override
                                public void onTimeout(String destination)
                                {
                                    System.out.println("timeout");
                                }


                                @Override
                                public void onFailure(Throwable t)
                                {
                                    System.out.println("Failed");
                                    System.out.println(t.getMessage());
                                }
                            });
                        }

                        catch (Exception ex)
                        {
                            System.out.println(ex.getMessage());
                        }
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
                        System.out.println("Disconnected");
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
