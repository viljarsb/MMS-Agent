package Agent.TLSConfiguration;

import lombok.NonNull;
import org.eclipse.jetty.util.ssl.SslContextFactory;


/**
 * Configuration object for mutual TLS communication,
 * must be used when connect to the edge router in authenticated mode.
 */
public class mTLSConfiguration extends TLSConfiguration
{
    private final String keyStorePath;
    private final String keyStorePassword;


    /**
     * Constructs a mutual TLS configuration with the specified trust store and key store paths and passwords.
     *
     * @param trustStorePath     the path to the trust store file
     * @param trustStorePassword the password for the trust store
     * @param keyStorePath       the path to the key store file
     * @param keyStorePassword   the password for the key store
     * @throws NullPointerException if any of the arguments is null
     */
    public mTLSConfiguration(@NonNull String trustStorePath, @NonNull String trustStorePassword, @NonNull String keyStorePath, @NonNull String keyStorePassword)
    {
        super(trustStorePath, trustStorePassword);
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
    }


    /**
     * Returns an SSL context factory for mutual TLS communication.
     *
     * @return an SSL context factory
     */
    @Override
    public SslContextFactory.Client getTLSContextFactory()
    {
        SslContextFactory.Client sslContextFactory = super.getTLSContextFactory();
        sslContextFactory.setKeyStorePath(keyStorePath);
        sslContextFactory.setKeyStorePassword(keyStorePassword);
        sslContextFactory.setKeyManagerPassword(keyStorePassword);
        sslContextFactory.setKeyManagerFactoryAlgorithm("PKIX");
        sslContextFactory.setTrustManagerFactoryAlgorithm("PKIX");
        sslContextFactory.setNeedClientAuth(true);
        return sslContextFactory;
    }
}
