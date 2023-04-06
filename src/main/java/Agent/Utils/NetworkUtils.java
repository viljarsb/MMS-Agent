package Agent.Utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * This class provides utility methods for resolving network addresses.
 */
public class NetworkUtils
{

    /**
     * Attempts to find the default network interface and returns its first available InetAddress.
     *
     * @return the default InetAddress
     * @throws UnknownHostException if no default InetAddress is found
     */
    public static InetAddress getDefaultInetAddress() throws UnknownHostException
    {
        try
        {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements())
            {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (isValidNetworkInterface(networkInterface))
                {
                    InetAddress defaultInetAddress = getFirstInetAddress(networkInterface);

                    if (defaultInetAddress != null)
                    {
                        return defaultInetAddress;
                    }
                }
            }
        }
        catch (SocketException e)
        {
            throw new UnknownHostException("Error getting network interfaces: " + e.getMessage());
        }

        throw new UnknownHostException("No default InetAddress found.");
    }


    /**
     * Checks whether the given network interface is valid for use in finding a default InetAddress.
     *
     * @param networkInterface the network interface to check
     * @return true if the network interface is valid, false otherwise
     * @throws SocketException if an error occurs while checking the network interface
     */
    private static boolean isValidNetworkInterface(NetworkInterface networkInterface) throws SocketException
    {
        return networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.isVirtual();
    }


    /**
     * Returns the first available InetAddress for the given network interface.
     *
     * @param networkInterface the network interface to get the InetAddress for
     * @return the first available InetAddress for the network interface, or null if none are available
     */
    private static InetAddress getFirstInetAddress(NetworkInterface networkInterface)
    {
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        return inetAddresses.hasMoreElements() ? inetAddresses.nextElement() : null;
    }


    /**
     * Resolves the InetAddress for the given address string, or returns the default InetAddress if the address
     * string is null or empty.
     *
     * @param address the address string to resolve
     * @return the resolved InetAddress
     * @throws UnknownHostException if the address cannot be resolved
     */
    public static InetAddress resolveInetAddress(String address) throws UnknownHostException
    {
        if (address == null || address.isEmpty())
        {
            return getDefaultInetAddress();
        }

        InetAddress inetAddress = InetAddress.getByName(address);
        if (inetAddress == null)
        {
            throw new UnknownHostException("Failed to resolve InetAddress for the given address: " + address);
        }

        return inetAddress;
    }
}