package io.github.tbondetti.authserver.security.oauth2;

import com.nimbusds.jose.jwk.RSAKey;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import io.github.tbondetti.authserver.security.properties.JwtKeyStoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ERREUR_TECHNIQUE;

@Component
@RequiredArgsConstructor
public class RsaKeysLoader {

    static final String KEYSTORE_TYPE = "PKCS12";

    static final String ERREUR_CHARGEMENT_CLE_RSA = "Erreur lors du chargement de la clé RSA";
    static final String ERREUR_INIT_KEY_STORE = "Erreur lors de l'initialisation du key store.";
    static final String ERREUR_LOADING_KEY_STORE = "Erreur lors du chargement du key store.";
    static final String ERREUR_RECUPERATION_CLE_PRIVEE = "Erreur lors de la récupération de la clé privée %s.";
    static final String ERREUR_GET_CERTIFICATE = "Erreur lors du chargement du certificat de la clé publique RSA %s.";
    static final String ERREUR_CERTIFICATE_NOT_FOUND = "Certificat introuvable pour la clé %s.";
    static final String ERREUR_CLE_PRIVEE_INTROUVABLE = "Clé privée %s introuvable.";
    static final String ERREUR_PRIVATE_KEY_BAD_FORMAT = "Format de clé privée %s incorrect.";
    static final String ERREUR_PUBLIC_KEY_BAD_FORMAT = "Format de clé publique %s incorrect.";


    private final ResourceLoader resourceLoader;
    private final JwtKeyStoreProperties jwtKeyStoreProperties;

    public List<RSAKey> loadAll() {
        try (final InputStream inputStream = this.resourceLoader
                .getResource(this.jwtKeyStoreProperties.location())
                .getInputStream()
        ) {
            return this.loadAllRsaKeys(inputStream);
        } catch (final IOException e) {
            throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_CHARGEMENT_CLE_RSA, e);
        }
    }

    List<RSAKey> loadAllRsaKeys(final InputStream inputStream) {
        final KeyStore keyStore = loadKeyStore(inputStream,  this.jwtKeyStoreProperties.storePassword());

        return this.jwtKeyStoreProperties.keys().entrySet()
                .stream()
                .map(entry -> buildRsaKey(keyStore, entry.getKey(), entry.getValue()))
                .toList()
                ;
    }

    static RSAKey buildRsaKey(
            final KeyStore keyStore,
            final String alias,
            final String keyPassword
    ) {
        final RSAPrivateKey privateKey = rsaPrivateKey(keyStore, alias, keyPassword);
        final RSAPublicKey publicKey = rsaPublicKey(keyStore, alias);

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(alias)
                .build();
    }

    static KeyStore loadKeyStore(
            final InputStream inputStream,
            final String keyStorePassword
    ) {
        final KeyStore keyStore = keyStore();

        loadKeyStore(inputStream, keyStore, keyStorePassword);

        return keyStore;
    }

    static KeyStore keyStore() {
        try {
            return KeyStore.getInstance(KEYSTORE_TYPE);
        } catch (final KeyStoreException e) {
            throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_INIT_KEY_STORE, e);
        }
    }

    static void loadKeyStore(
            final InputStream inputStream,
            final KeyStore keyStore,
            final String keyStorePassword
    ) {
        try {
            keyStore.load(inputStream, keyStorePassword.toCharArray());
        } catch (final IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_LOADING_KEY_STORE, e);
        }
    }

    static RSAPrivateKey rsaPrivateKey(
            final KeyStore keyStore,
            final String alias,
            final String keyPassword
    ) {
        try {
            final Key privateKey = keyStore.getKey(alias, keyPassword.toCharArray());

            if (privateKey == null) {
                throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_CLE_PRIVEE_INTROUVABLE.formatted(alias));
            }

            if (!(privateKey instanceof RSAPrivateKey)) {
                throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_PRIVATE_KEY_BAD_FORMAT.formatted(alias));
            }

            return (RSAPrivateKey) privateKey;
        } catch (final KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_RECUPERATION_CLE_PRIVEE.formatted(alias), e);
        }
    }

    static RSAPublicKey rsaPublicKey(
            final KeyStore keyStore,
            final String alias
    ) {
        final Certificate certificate = rsaPublicKeyCertificate(keyStore, alias);

        if (certificate == null) {
            throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_CERTIFICATE_NOT_FOUND.formatted(alias));
        }

        final PublicKey publicKey = certificate.getPublicKey();

        if (!(publicKey instanceof RSAPublicKey)) {
            throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_PUBLIC_KEY_BAD_FORMAT.formatted(alias));
        }

        return (RSAPublicKey) publicKey;
    }

    static Certificate rsaPublicKeyCertificate(
            final KeyStore keyStore,
            final String alias
    ) {
        try {
            return keyStore.getCertificate(alias);
        } catch (final KeyStoreException e) {
            throw new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_GET_CERTIFICATE.formatted(alias), e);
        }
    }

}