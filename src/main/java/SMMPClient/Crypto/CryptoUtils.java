package SMMPClient.Crypto;

import SMMPClient.Exceptions.DecryptionException;
import SMMPClient.Exceptions.EncryptionException;
import SMMPClient.Exceptions.SignatureGenerationException;
import SMMPClient.Exceptions.SignatureVerificationException;
import lombok.NonNull;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;


/**
 * A utility class for cryptographic operations such as encryption, decryption, signing, and verification.
 * Uses the BouncyCastle provider for cryptographic operations.
 */
public class CryptoUtils
{
    private static final int KEY_LENGTH = 256; // bits
    private static final int IV_LENGTH = 128; // bits
    private static final String HKDF_ALGORITHM = "HkdfSHA512";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    private static final String SIGNATURE_ALGORITHM = "SHA512withECDSA";
    private static final String PROVIDER_NAME = "BC";
    private static final byte[] HKDF_INFO = "SMMPv1".getBytes();

    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    private CryptoUtils() {}


    /**
     * Encrypts a message using the provided public and private keys.
     *
     * @param publicKey  The public key used for encryption.
     * @param privateKey The private key used for encryption.
     * @param message    The message to be encrypted.
     * @return The encrypted message.
     * @throws EncryptionException If an error occurs during encryption.
     */
    public static byte[] encryptMessage(@NonNull ECPublicKey publicKey, @NonNull ECPrivateKey privateKey, @NonNull byte[] message) throws EncryptionException
    {
        try
        {
            // Derive shared secret
            byte[][] ikmAndSalt = deriveSecret(publicKey, privateKey);
            byte[] ikm = ikmAndSalt[0];
            byte[] salt = ikmAndSalt[1];

            // Derive key and IV using HKDF
            byte[][] keyAndIV = hkdf(ikm, salt);
            byte[] key = keyAndIV[0];
            byte[] iv = keyAndIV[1];

            // Encrypt message using AES-CBC
            return encrypt(key, iv, message);
        }

        catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException |
               BadPaddingException ex)
        {
            throw new EncryptionException("Failed to encrypt message", ex);
        }
    }


    /**
     * Decrypts a message using the provided public and private keys.
     *
     * @param publicKey  The public key used for decryption.
     * @param privateKey The private key used for decryption.
     * @param message    The message to be decrypted.
     * @return The decrypted message.
     * @throws DecryptionException If an error occurs during decryption.
     */
    public static byte[] decryptMessage(@NonNull ECPublicKey publicKey, @NonNull ECPrivateKey privateKey, @NonNull byte[] message) throws DecryptionException
    {

        try
        {
            // Derive shared secret
            byte[][] ikmAndSalt = deriveSecret(publicKey, privateKey);
            byte[] ikm = ikmAndSalt[0];
            byte[] salt = ikmAndSalt[1];

            // Derive key and IV using HKDF
            byte[][] keyAndIV = hkdf(ikm, salt);
            byte[] key = keyAndIV[0];
            byte[] iv = keyAndIV[1];

            // Decrypt message using AES-CBC
            return decrypt(key, iv, message);
        }

        catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException |
               BadPaddingException ex)
        {
            throw new DecryptionException("Failed to decrypt message", ex);
        }
    }


    /**
     * Signs the provided data using the provided private key.
     *
     * @param data The data to be signed.
     * @param key  The private key used for signing.
     * @return The signature.
     * @throws SignatureGenerationException If an error occurs during signing.
     */
    public static byte[] sign(@NonNull ECPrivateKey key, @NonNull byte[] data) throws SignatureGenerationException
    {
        try
        {
            Signature signer = Signature.getInstance(SIGNATURE_ALGORITHM, PROVIDER_NAME);
            signer.initSign(key);
            signer.update(data);
            return signer.sign();
        }

        catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException ex)
        {
            throw new SignatureGenerationException("Failed to sign data", ex);
        }
    }


    /**
     * Verifies the provided signature against the provided data using the provided public key.
     *
     * @param key       The public key used for signature verification.
     * @param data      The data to be verified.
     * @param signature The signature to be verified.
     * @return true if the signature is valid, false otherwise.
     * @throws SignatureVerificationException If an error occurs during signature verification.
     */
    public static boolean verifySignature(@NonNull ECPublicKey key, @NonNull byte[] data, @NonNull byte[] signature) throws SignatureVerificationException
    {
        try
        {
            Signature verifier = Signature.getInstance(SIGNATURE_ALGORITHM, PROVIDER_NAME);
            verifier.initVerify(key);
            verifier.update(data);
            return verifier.verify(signature);
        }

        catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException ex)
        {
            throw new SignatureVerificationException("Failed to verify signature", ex);
        }
    }


    /**
     * Derives a shared secret from the provided public and private keys.
     *
     * @param publicKey  The public key.
     * @param privateKey The private key.
     * @return An array containing the derived shared secret and the salt.
     * @throws NoSuchAlgorithmException if the key agreement algorithm is not available.
     * @throws NoSuchProviderException  if the security provider is not available.
     * @throws InvalidKeyException      if the provided key is invalid.
     */
    private static byte[][] deriveSecret(ECPublicKey publicKey, ECPrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException
    {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", PROVIDER_NAME);
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);
        byte[] sharedSecret = keyAgreement.generateSecret();

        byte[] ikm = new byte[KEY_LENGTH / 8];
        byte[] salt = new byte[IV_LENGTH / 8];

        System.arraycopy(sharedSecret, 0, ikm, 0, ikm.length);
        System.arraycopy(sharedSecret, ikm.length, salt, 0, salt.length);

        return new byte[][]{ikm, salt};
    }


    /**
     * Derives a key and an initialization vector (IV) from the provided input key material and salt using the HKDF algorithm.
     *
     * @param ikm  The input key material.
     * @param salt The salt.
     * @return An array containing the derived key and the IV.
     */
    private static byte[][] hkdf(byte[] ikm, byte[] salt)
    {
        HKDFParameters hkdfParams = new HKDFParameters(ikm, salt, HKDF_INFO);
        HKDFBytesGenerator hkdfBytesGen = new HKDFBytesGenerator(new SHA512Digest());
        hkdfBytesGen.init(hkdfParams);

        byte[] key = new byte[KEY_LENGTH / 8];
        byte[] iv = new byte[IV_LENGTH / 8];

        hkdfBytesGen.generateBytes(key, 0, key.length);
        hkdfBytesGen.generateBytes(iv, 0, iv.length);

        return new byte[][]{key, iv};
    }


    /**
     * Encrypts data using the provided key and IV.
     *
     * @param key  The key.
     * @param iv   The IV.
     * @param data The data to be encrypted.
     * @return The encrypted data.
     * @throws NoSuchPaddingException             if the padding scheme is not available.
     * @throws NoSuchAlgorithmException           if the encryption algorithm is not available.
     * @throws NoSuchProviderException            if the security provider is not available.
     * @throws InvalidAlgorithmParameterException if the encryption algorithm parameters are invalid.
     * @throws InvalidKeyException                if the provided key is invalid.
     * @throws IllegalBlockSizeException          if the block size is invalid.
     * @throws BadPaddingException                if the padding is invalid.
     */
    private static byte[] encrypt(byte[] key, byte[] iv, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        return cipher.doFinal(data);
    }


    /**
     * Decrypts data using the provided key and IV.
     *
     * @param key  The key.
     * @param iv   The IV.
     * @param data The data to be decrypted.
     * @return The decrypted data.
     * @throws IllegalBlockSizeException          if the block size is invalid.
     * @throws BadPaddingException                if the padding is invalid.
     * @throws NoSuchPaddingException             if the padding scheme is not available.
     * @throws NoSuchAlgorithmException           if the encryption algorithm is not available.
     * @throws NoSuchProviderException            if the security provider is not available.
     * @throws InvalidAlgorithmParameterException if the encryption algorithm parameters are invalid.
     * @throws InvalidKeyException                if the provided key is invalid.
     */
    private static byte[] decrypt(byte[] key, byte[] iv, byte[] data) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException
    {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        return cipher.doFinal(data);
    }
}
