package Agent.TLSConfiguration;

import lombok.NonNull;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * TLS configuration object, must be used when connecting to the edge router in anonymous mode.
 */
public class TLSConfiguration
{
    protected String trustStorePath;
    protected String trustStorePassword;


    /**
     * Constructs a TLS configuration with the specified trust store path and password.
     * @param trustStorePath the path to the trust store file
     * @param trustStorePassword the password for the trust store
     * @throws NullPointerException if any of the arguments is null
     */
    public TLSConfiguration(@NonNull String trustStorePath, @NonNull String trustStorePassword)
    {
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
    }

    /**
     * Returns an SSL context factory for TLS communication.
     * @return an SSL context factory
     */
    public SslContextFactory.Client getTLSContextFactory()
    {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();

        sslContextFactory.setTrustStorePath(trustStorePath);
        sslContextFactory.setTrustStorePassword(trustStorePassword);
        sslContextFactory.setTrustManagerFactoryAlgorithm("PKIX");
        sslContextFactory.setProvider("BC");
        sslContextFactory.setProtocol("TLSv1.3");
        sslContextFactory.setEndpointIdentificationAlgorithm(null);

        return sslContextFactory;
    }
}
