package Agent.ServiceDiscovery;

import java.util.List;

/**
 * Interface for a listener that is notified when new router services are discovered.
 */
public interface IDiscoveryListener {
    /**
     * Called when new router services are discovered.
     *
     * @param routerInfos the list of discovered {@link RouterInfo} objects.
     */
    void onDiscovered(List<RouterInfo> routerInfos);
}
