package SMMPClient.Crypto;

import SMMPClient.Exceptions.CertificateValidationException;
import SMMPClient.Exceptions.MissingCertificateException;

import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

/**
 * Interface that keyring managers should implement.
 */
public interface IKeyringManager {
    /**
     * Gets my private key.
     *
     * @return My private key
     */
    ECPrivateKey getMyPrivateKey();

    /**
     * Gets my public key.
     *
     * @return My public key
     */
    ECPublicKey getMyPublicKey();

    /**
     * Gets my certificate.
     *
     * @return My certificate
     */
    X509Certificate getMyCertificate();

    /**
     * Gets the public key of a given MRN from the keyring.
     *
     * @param mrn The MRN of the public key to get
     * @return The public key of the given MRN
     * @throws KeyStoreException              If there's a problem loading the keyring
     * @throws MissingCertificateException    If the keyring doesn't contain a public key for the given MRN
     * @throws CertificateValidationException If the certificate for the given MRN is not trusted
     */
    ECPublicKey getPublicKey(String mrn) throws KeyStoreException, MissingCertificateException, CertificateValidationException;

    /**
     * Verifies the given certificate against the truststore. If the certificate is trusted and
     * not present in the truststore, it will be saved to the truststore.
     *
     * @param certificate The certificate to verify
     * @return True if the certificate is trusted, false otherwise
     * @throws CertificateValidationException If an error occurs while verifying the certificate
     * @throws KeyStoreException              If an error occurs while loading the truststore
     */
    boolean verifyCertificate(X509Certificate certificate) throws CertificateValidationException, KeyStoreException;

    /**
     * Gets the path of the truststore.
     *
     * @return The path of the truststore
     */
    String getTruststorePath();

    /**
     * Gets the password of the truststore.
     *
     * @return The password of the truststore
     */
    String getTruststorePassword();

    /**
     * Gets the path of the keystore.
     *
     * @return The path of the keystore
     */
    String getKeystorePath();

    /**
     * Gets the password of the keystore.
     *
     * @return The password of the keystore
     */
    String getKeystorePassword();

    /**
     * Gets the path of the keyring.
     *
     * @return The path of the keyring
     */
    String getKeyringPath();

    /**
     * Gets the password of the keyring.
     *
     * @return The password of the keyring
     */
    String getKeyringPassword();
}