package Agent.ServiceDiscovery;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * The {@code mDNSDiscoveryService} class provides a way to discover available routers using mDNS.
 */
@Slf4j
public class mDNSRouterDiscoveryService implements ImDNSRouterDiscoveryService
{
    private static final String SERVICE_TYPE = "_mms-edge-router._tcp.local.";
    private static final long SERVICE_TIMEOUT = 6000;
    private final JmDNS jmDNS;
    private final Executor executor;


    /**
     * Creates an instance of {@code mDNSDiscoveryService}.
     *
     * @param address  the InetAddress to bind the JmDNS instance to
     * @param executor the Executor to use for running the mDNS discovery task
     * @throws IOException if an I/O error occurs while creating the JmDNS instance
     */
    public mDNSRouterDiscoveryService(@NonNull InetAddress address, @NonNull Executor executor) throws IOException
    {
        this.jmDNS = JmDNS.create(address);
        this.executor = executor;
        log.info("MdnsDiscoveryService instance created");
    }


    /**
     * Discovers available routers and notifies the specified listener.
     *
     * @param listener the listener to notify
     */
    @Override
    public void listen(@NonNull IDiscoveryListener listener)
    {
        executor.execute(() ->
        {
            ServiceInfo[] serviceInfos = jmDNS.list(SERVICE_TYPE, SERVICE_TIMEOUT);
            log.info("Found {} available routers.", serviceInfos.length);
            listener.onDiscovered(assembleRouterInfo(serviceInfos));
        });
    }


    /**
     * Discovers available routers and returns them in a CompletableFuture.
     *
     * @return a CompletableFuture that will complete with a list of available routers
     */
    @Override
    public CompletableFuture<List<RouterInfo>> listen()
    {
        return CompletableFuture.supplyAsync(() ->
        {
            ServiceInfo[] serviceInfos = jmDNS.list(SERVICE_TYPE, SERVICE_TIMEOUT);
            log.info("Found {} available routers.", serviceInfos.length);
            return assembleRouterInfo(serviceInfos);
        }, executor);
    }


    /**
     * Unregisters all services and closes the JmDNS instance.
     */
    @Override
    public void close()
    {
        try
        {
            jmDNS.unregisterAllServices();
            jmDNS.close();
        }
        catch (IOException ex)
        {
            log.error("Failed to close MdnsDiscoveryService", ex);
        }
    }


    /**
     * Assembles a list of RouterInfo objects from the specified ServiceInfo array.
     *
     * @param serviceInfos the ServiceInfo array to assemble the RouterInfo objects from
     * @return a list of RouterInfo objects
     */
    private List<RouterInfo> assembleRouterInfo(@NonNull ServiceInfo[] serviceInfos)
    {
        return Arrays.stream(serviceInfos).map(RouterInfo::new).collect(Collectors.toList());
    }
}
