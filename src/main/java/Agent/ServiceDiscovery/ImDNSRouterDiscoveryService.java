package Agent.ServiceDiscovery;

import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for an mDNS router discovery service that allows listening for router services and
 * retrieving information about them.
 */
public interface ImDNSRouterDiscoveryService
{
    /**
     * Registers a listener to be notified when new router services are discovered.
     *
     * @param listener the {@link IDiscoveryListener} to register.
     */
    void listen(@NonNull IDiscoveryListener listener);

    /**
     * Asynchronously retrieves a list of discovered router services.
     *
     * @return a {@link CompletableFuture} that completes with the list of {@link RouterInfo} objects.
     */
    CompletableFuture<List<RouterInfo>> listen();

    /**
     * Closes the mDNS router discovery service.
     */
    void close();
}