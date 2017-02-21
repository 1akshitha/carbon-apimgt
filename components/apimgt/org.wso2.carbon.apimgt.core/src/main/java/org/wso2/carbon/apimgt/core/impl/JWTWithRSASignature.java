package org.wso2.carbon.apimgt.core.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.wso2.carbon.apimgt.core.exception.NoSuchKeyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

/**
 * RSA signatures require a public and private RSA key pair. Get the private key from keyStore to sign and
 * get the public key from trustStore to verify the validity of the signature.
 */
public class JWTWithRSASignature {

    private JWTWithRSASignature() {

    }

    // To get private key from key store
    public static PrivateKey getPrivateKey(String keyStoreFilePath, String keyStorePassword, String alias,
                                           String aliasPassword) throws NoSuchKeyException {
        if (keyStoreFilePath == null) {
            throw new IllegalArgumentException("Path to key store file must not be null");
        }
        if (keyStorePassword == null) {
            throw new IllegalArgumentException("The key store password must not be null");
        }
        if (alias == null) {
            throw new IllegalArgumentException("The Alias must not be null");
        }
        if (aliasPassword == null) {
            throw new IllegalArgumentException("The Alias password not be null");
        }
        Key key;
        try (InputStream inputStream = new FileInputStream(keyStoreFilePath)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(inputStream, keyStorePassword.toCharArray());
            key = keyStore.getKey(alias, aliasPassword.toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | CertificateException |
                IOException e) {
            throw new NoSuchKeyException("Private key not found", e);
        }
        if (!(key instanceof PrivateKey)) {
            throw new NoSuchKeyException("Private key not found");
        }
        return (PrivateKey) key;
    }

    // To sign the JWT using RSA and serialize
    public static String rsaSignAndSerialize(RSAPrivateKey rsaPrivateKey, JWTClaimsSet claimsSet) throws
            JOSEException {
        if (rsaPrivateKey == null) {
            throw new IllegalArgumentException("The private key must not be null");
        }
        if (claimsSet == null) {
            throw new IllegalArgumentException("The JWTClaimsSet must not be null");
        }
        JWSSigner signer = new RSASSASigner(rsaPrivateKey);
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        jwt.sign(signer);
        return jwt.serialize();
    }

    // To get public key from trustStore and verify the validity of the signature
    public static PublicKey getPublicKey(String keyStoreFilePath, String keyStorePassword, String alias) throws
            NoSuchKeyException {
        if (keyStoreFilePath == null) {
            throw new IllegalArgumentException("Path to key store file must not be null");
        }
        if (keyStorePassword == null) {
            throw new IllegalArgumentException("The key store password must not be null");
        }
        if (alias == null) {
            throw new IllegalArgumentException("The Alias must not be null");
        }

        Certificate cert;
        try (FileInputStream inputStream = new FileInputStream(keyStoreFilePath)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(inputStream, keyStorePassword.toCharArray());
            cert = keyStore.getCertificate(alias);
        } catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
            throw new NoSuchKeyException("Public key not found", e);
        }
        return cert.getPublicKey();
    }

    // To verify the signature
    public static boolean verifyRSASignature(String token, RSAPublicKey rsaPublicKey) throws ParseException,
            JOSEException {
        if (token == null) {
            throw new IllegalArgumentException("The SignedJWT must not be null");
        }
        if (rsaPublicKey == null) {
            throw new IllegalArgumentException("The public key must not be null");
        }
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new RSASSAVerifier(rsaPublicKey);
        return signedJWT.verify(verifier);
    }
}
