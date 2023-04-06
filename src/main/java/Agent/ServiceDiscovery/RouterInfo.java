package Agent.ServiceDiscovery;

import lombok.NonNull;

import javax.jmdns.ServiceInfo;
import java.net.URI;

/**
 * The {@code RouterInfo} class provides a convenient way to store information about
 * a discovered router service and extract the information needed to connect to the
 * router service.
 */
public class RouterInfo {
    private final String serviceName;
    private final String serviceIP;
    private final int servicePort;
    private final String servicePath;

    /**
     * Creates a new {@code RouterInfo} object based on the given {@link ServiceInfo}.
     *
     * @param serviceInfo the {@link ServiceInfo} to use.
     */
    public RouterInfo(@NonNull ServiceInfo serviceInfo) {
        this.serviceName = serviceInfo.getName();
        this.serviceIP = serviceInfo.getHostAddresses()[0];
        this.servicePort = serviceInfo.getPort();
        this.servicePath = serviceInfo.getPropertyString("path");
    }

    /**
     * Creates a new {@code RouterInfo} object with the given parameters.
     *
     * @param serviceName the name of the router service.
     * @param serviceIP the IP address of the router service.
     * @param servicePort the port number of the router service.
     * @param servicePath the path of the router service.
     */
    public RouterInfo(@NonNull String serviceName, @NonNull String serviceIP,
                      @NonNull Integer servicePort, @NonNull String servicePath) {
        this.serviceName = serviceName;
        this.serviceIP = serviceIP;
        this.servicePort = servicePort;
        this.servicePath = servicePath;
    }

    /**
     * Returns the name of the router service.
     *
     * @return the name of the router service.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns the IP address of the router service.
     *
     * @return the IP address of the router service.
     */
    public String getServiceIP() {
        return serviceIP;
    }

    /**
     * Returns the port number of the router service.
     *
     * @return the port number of the router service.
     */
    public int getServicePort() {
        return servicePort;
    }

    /**
     * Returns the path of the router service.
     *
     * @return the path of the router service.
     */
    public String getServicePath() {
        return servicePath;
    }

    /**
     * Returns a URI for the router service based on the IP address, port number, and path.
     *
     * @return a URI for the router service.
     */
    public URI getURI()
    {
        return URI.create("wss://" + serviceIP + ":" + servicePort + servicePath);
    }


    /**
     * Returns a string representation of the {@code RouterInfo} object.
     *
     * @return a string representation of the {@code RouterInfo} object.
     */
    @Override
    public String toString()
    {
        return "RouterInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceIP='" + serviceIP + '\'' +
                ", servicePort=" + servicePort +
                ", servicePath='" + servicePath + '\'' +
                '}';
    }
}
