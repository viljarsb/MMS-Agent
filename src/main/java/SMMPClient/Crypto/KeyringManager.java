package SMMPClient.Crypto;

import Agent.Exceptions.MMSSecurityException;
import SMMPClient.Exceptions.CertificateValidationException;
import SMMPClient.Exceptions.MissingCertificateException;
import lombok.NonNull;
import net.maritimeconnectivity.pki.CertificateHandler;
import net.maritimeconnectivity.pki.PKIIdentity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Enumeration;


/**
 * The KeyringManager class manages cryptographic keys and certificates used for secure communication.
 * It provides functionality to load, store, and retrieve keys and certificates, as well as verifying
 * certificates against a truststore. This class is particularly helpful in handling the keys and
 * certificates for both the local user and remote entities, identified by their MRNs (Maritime Resource Names).
 * <p>
 * The class is designed with a private constructor and a factory method {@code create()} for instantiation,
 * ensuring that the object is properly set up before use.
 */
public class KeyringManager implements IKeyringManager
{
    private final ECPrivateKey myPrivateKey;
    private final ECPublicKey myPublicKey;
    private final X509Certificate myCertificate;

    private final String ksPath;
    private final String ksPassword;

    private final String tsPath;
    private final String tsPass;

    private final String keyringPath;
    private final String keyringPassword;


    /**
     * Constructor for {@link KeyringManager}.
     */
    private KeyringManager(String ksPath, String ksPassword, ECPublicKey publicKey, ECPrivateKey privateKey, X509Certificate certificate, String tsPath, String tsPass, String keyringPath, String keyringPassword)
    {
        this.myPrivateKey = privateKey;
        this.myPublicKey = publicKey;
        this.myCertificate = certificate;

        this.ksPath = ksPath;
        this.ksPassword = ksPassword;

        this.tsPath = tsPath;
        this.tsPass = tsPass;

        this.keyringPath = keyringPath;
        this.keyringPassword = keyringPassword;
    }


    /**
     * Creates a new instance of {@link KeyringManager}.
     *
     * @return A new KeyringManager instance
     * @throws MMSSecurityException If there's a problem setting up the keyring manager
     */
    public static KeyringManager create(@NonNull String ksPath, @NonNull String ksPassword, @NonNull String tsPath, @NonNull String tsPass, @NonNull String keyringPath, @NonNull String keyringPassword) throws MMSSecurityException
    {
        try
        {
            KeyStore keyStore = loadKeyStore(ksPath, ksPassword);
            ECPrivateKey privateKey = loadPrivateKey(keyStore, ksPassword);
            ECPublicKey publicKey = loadPublicKey(keyStore);
            X509Certificate certificate = loadCertificate(keyStore);

            if (privateKey == null || publicKey == null || certificate == null)
            {
                throw new MMSSecurityException("Failed to load private key, public key, or certificate");
            }

            return new KeyringManager(ksPath, ksPassword, publicKey, privateKey, certificate, tsPath, tsPass, keyringPath, keyringPassword);
        }
        catch (Exception ex)
        {
            throw new MMSSecurityException("Failed to setup keyring manager", ex);
        }
    }


    /**
     * Gets my private key.
     *
     * @return My private key
     */
    @Override
    public ECPrivateKey getMyPrivateKey()
    {
        return myPrivateKey;
    }


    /**
     * Gets my public key.
     *
     * @return My public key
     */
    @Override
    public ECPublicKey getMyPublicKey()
    {
        return myPublicKey;
    }


    /**
     * Gets my certificate.
     *
     * @return My certificate
     */
    @Override
    public X509Certificate getMyCertificate()
    {
        return myCertificate;
    }


    /**
     * Gets the public key of a given MRN from the keyring.
     *
     * @param mrn The MRN of the public key to get
     * @return The public key of the given MRN
     * @throws KeyStoreException              If there's a problem loading the keyring
     * @throws MissingCertificateException    If the keyring doesn't contain a public key for the given MRN
     * @throws CertificateValidationException If the certificate for the given MRN is not trusted
     */
    @Override
    public ECPublicKey getPublicKey(@NonNull String mrn) throws KeyStoreException, MissingCertificateException, CertificateValidationException
    {
        KeyStore keyring = loadKeyring();
        X509Certificate certificate = getCertificateFromKeyring(keyring, mrn);

        if (certificate != null)
        {
            if (verifyCertificate(certificate))
            {
                return (ECPublicKey) certificate.getPublicKey();
            }
            else
            {
                throw new CertificateValidationException("Certificate not trusted for MRN: " + mrn);
            }
        }
        else
        {
            throw new MissingCertificateException("No certificate found for MRN: " + mrn);
        }
    }


    /**
     * Verifies the given certificate against the truststore. If the certificate is trusted and
     * not present in the truststore, it will be saved to the truststore.
     *
     * @param certificate The certificate to verify
     * @return True if the certificate is trusted, false otherwise
     * @throws CertificateValidationException If an error occurs while verifying the certificate
     * @throws KeyStoreException              If an error occurs while loading the truststore
     */
    @Override
    public boolean verifyCertificate(@NonNull X509Certificate certificate) throws CertificateValidationException, KeyStoreException
    {
        KeyStore trustStore = loadTrustStore();

        if (verifyCertificateChain(certificate, trustStore))
        {
            PKIIdentity identity = CertificateHandler.getIdentityFromCert(certificate);
            String MRN = identity.getMrn();

            if (trustStore.getCertificateAlias(certificate) == null)
            {
                try (FileOutputStream fos = new FileOutputStream(tsPath))
                {
                    trustStore.setCertificateEntry(MRN, certificate);
                    trustStore.store(fos, tsPass.toCharArray());
                }

                catch (IOException | NoSuchAlgorithmException | CertificateException ignored)
                {
                }
            }

            return true;
        }

        return false;
    }


    /**
     * Gets the path of the truststore.
     *
     * @return The path of the truststore
     */
    @Override
    public String getTruststorePath()
    {
        return tsPath;
    }


    /**
     * Gets the password of the truststore.
     *
     * @return The password of the truststore
     */
    @Override
    public String getTruststorePassword()
    {
        return tsPass;
    }


    /**
     * Gets the path of the keystore.
     *
     * @return The path of the keystore
     */
    @Override
    public String getKeystorePath()
    {
        return ksPath;
    }


    /**
     * Gets the password of the keystore.
     *
     * @return The password of the keystore
     */
    @Override
    public String getKeystorePassword()
    {
        return ksPassword;
    }


    /**
     * Gets the path of the keyring.
     *
     * @return The path of the keyring
     */
    @Override
    public String getKeyringPath()
    {
        return keyringPath;
    }


    /**
     * Gets the password of the keyring.
     *
     * @return The password of the keyring
     */
    @Override
    public String getKeyringPassword()
    {
        return keyringPassword;
    }


    /**
     * Loads a PKCS12 keystore from the given path and password.
     *
     * @param path     The path to the keystore
     * @param password The password to the keystore
     * @return The loaded keystore
     * @throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException
     */
    private static KeyStore loadKeyStore(String path, String password) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException
    {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis = new FileInputStream(path))
        {
            keyStore.load(fis, password.toCharArray());
        }

        return keyStore;
    }


    /**
     * Loads my private key from the given keystore.
     *
     * @return The private key
     * @throws UnrecoverableKeyException If the key cannot be recovered
     * @throws NoSuchAlgorithmException  If the algorithm for recovering the key cannot be found
     * @throws KeyStoreException         If an error occurs while getting the private key
     */
    private static ECPrivateKey loadPrivateKey(KeyStore keyStore, String password) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException
    {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements())
        {
            String alias = aliases.nextElement();
            Key key = keyStore.getKey(alias, password.toCharArray());
            if (key instanceof ECPrivateKey)
            {
                return (ECPrivateKey) key;
            }
        }
        return null;
    }


    /**
     * Loads my public key from the given keystore.
     *
     * @return The public key
     * @throws KeyStoreException If an error occurs while getting the public key
     */
    private static ECPublicKey loadPublicKey(KeyStore keyStore) throws KeyStoreException
    {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements())
        {
            String alias = aliases.nextElement();
            Certificate cert = keyStore.getCertificate(alias);
            if (cert != null)
            {
                PublicKey publicKey = cert.getPublicKey();
                if (publicKey instanceof ECPublicKey)
                {
                    return (ECPublicKey) publicKey;
                }
            }
        }
        return null;
    }


    /**
     * Loads my certificate from the given keystore.
     *
     * @return The certificate
     * @throws KeyStoreException If an error occurs while getting the certificate
     */
    private static X509Certificate loadCertificate(KeyStore keyStore) throws KeyStoreException
    {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements())
        {
            String alias = aliases.nextElement();
            Certificate cert = keyStore.getCertificate(alias);
            if (cert != null)
            {
                if (cert instanceof X509Certificate)
                {
                    return (X509Certificate) cert;
                }
            }
        }
        return null;
    }


    /**
     * Loads the keyring from the given path and password.
     *
     * @return The loaded keyring
     * @throws KeyStoreException If the keyring could not be loaded
     */
    private KeyStore loadKeyring() throws KeyStoreException
    {
        try
        {
            return loadKeyStore(keyringPath, keyringPassword);
        }
        catch (IOException | NoSuchAlgorithmException | CertificateException ex)
        {
            throw new KeyStoreException("Failed to load keyring", ex);
        }
    }


    /**
     * Gets the X509 certificate for the given MRN from the keyring.
     *
     * @param keyring The keyring to get the certificate from
     * @param mrn     The MRN of the certificate to get
     * @return The certificate for the given MRN
     * @throws KeyStoreException If an error occurs while getting the certificate
     */
    private X509Certificate getCertificateFromKeyring(KeyStore keyring, String mrn) throws KeyStoreException
    {
        try
        {
            return (X509Certificate) keyring.getCertificate(mrn);
        }
        catch (KeyStoreException ex)
        {
            throw new KeyStoreException("Failed to get certificate from keyring", ex);
        }
    }


    /**
     * Loads the truststore from the given path and password.
     *
     * @return The loaded truststore
     * @throws KeyStoreException If the truststore could not be loaded
     */
    private KeyStore loadTrustStore() throws KeyStoreException
    {
        try
        {
            return loadKeyStore(tsPath, tsPass);
        }
        catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex)
        {
            throw new KeyStoreException("Failed to load truststore", ex);
        }
    }


    /**
     * Verifies the given certificate chain against the given truststore.
     *
     * @param certificate The certificate to verify
     * @param trustStore  The truststore to verify the certificate against
     * @return True if the certificate is trusted, false otherwise
     * @throws CertificateValidationException If an error occurs while verifying the certificate
     */
    private boolean verifyCertificateChain(X509Certificate certificate, KeyStore trustStore) throws CertificateValidationException
    {
        try
        {
            return CertificateHandler.verifyCertificateChain(certificate, trustStore);
        }
        catch (CertPathValidatorException | InvalidAlgorithmParameterException | CertificateException | NoSuchAlgorithmException | KeyStoreException ex)
        {
            throw new CertificateValidationException("Failed to verify certificate", ex);
        }
    }
}