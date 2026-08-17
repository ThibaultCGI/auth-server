package io.github.tbondetti.authserver.security.oauth2;

import com.nimbusds.jose.jwk.RSAKey;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import io.github.tbondetti.authserver.security.properties.JwtKeyStoreProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ERREUR_TECHNIQUE;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_CERTIFICATE_NOT_FOUND;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_CHARGEMENT_CLE_RSA;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_CLE_PRIVEE_INTROUVABLE;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_GET_CERTIFICATE;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_INIT_KEY_STORE;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_LOADING_KEY_STORE;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_PRIVATE_KEY_BAD_FORMAT;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_PUBLIC_KEY_BAD_FORMAT;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.ERREUR_RECUPERATION_CLE_PRIVEE;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.KEYSTORE_TYPE;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.buildRsaKey;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.keyStore;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.loadKeyStore;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.rsaPrivateKey;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.rsaPublicKey;
import static io.github.tbondetti.authserver.security.oauth2.RsaKeysLoader.rsaPublicKeyCertificate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RsaKeysLoaderTest {

    @Spy
    @InjectMocks
    private RsaKeysLoader subject;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private JwtKeyStoreProperties jwtKeyStoreProperties;

    @Test
    void loadAllKo() throws IOException {
        final String location = "location";
        when(this.jwtKeyStoreProperties.location()).thenReturn(location);

        final Resource resource = mock(Resource.class);
        when(this.resourceLoader.getResource(location)).thenReturn(resource);

        final IOException ioException = new IOException("test");
        when(resource.getInputStream()).thenThrow(ioException);

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> this.subject.loadAll()
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertSame(ERREUR_CHARGEMENT_CLE_RSA, exception.getMessage());
        assertSame(ioException, exception.getCause());
    }

    @Test
    void loadAllOk() throws IOException {
        final String location = "location";
        when(this.jwtKeyStoreProperties.location()).thenReturn(location);

        final Resource resource = mock(Resource.class);
        when(this.resourceLoader.getResource(location)).thenReturn(resource);

        final InputStream inputStream = mock(InputStream.class);
        when(resource.getInputStream()).thenReturn(inputStream);

        final List<RSAKey> keys = List.of();
        doReturn(keys).when(this.subject).loadAllRsaKeys(inputStream);

        assertSame(keys, this.subject.loadAll());
    }

    @Test
    void loadAllRsaKeysOk() {
        final String storePassword = "storePassword";
        when(this.jwtKeyStoreProperties.storePassword()).thenReturn(storePassword);

        final InputStream inputStream = mock(InputStream.class);

        try (final MockedStatic<RsaKeysLoader> utilities = mockStatic(RsaKeysLoader.class, CALLS_REAL_METHODS)) {
            final KeyStore keyStore = mock(KeyStore.class);
            utilities.when(() -> loadKeyStore(inputStream, storePassword)).thenReturn(keyStore);

            final String key1 = "key1";
            final String value1 = "value1";

            final String key2 = "key2";
            final String value2 = "value2";

            final Map<String, String> keys = new HashMap<>() {{
                put(key1, value1);
                put(key2, value2);
            }};

            when(this.jwtKeyStoreProperties.keys()).thenReturn(keys);

            final RSAKey rsaKey1 = mock(RSAKey.class);
            utilities.when(() -> buildRsaKey(keyStore, key1, value1)).thenReturn(rsaKey1);

            final RSAKey rsaKey2 = mock(RSAKey.class);
            utilities.when(() -> buildRsaKey(keyStore, key2, value2)).thenReturn(rsaKey2);

            assertEquals(List.of(rsaKey1, rsaKey2), this.subject.loadAllRsaKeys(inputStream));
        }
    }

    @Test
    void buildRsaKeyOk() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";
        final String keyPassword = "keyPassword";

        try (final MockedStatic<RsaKeysLoader> utilities = mockStatic(RsaKeysLoader.class, CALLS_REAL_METHODS)) {
            final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);

            final KeyPair keyPair = generator.generateKeyPair();

            final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            utilities.when(() -> rsaPrivateKey(keyStore, alias, keyPassword)).thenReturn(privateKey);

            final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            utilities.when(() -> rsaPublicKey(keyStore, alias)).thenReturn(publicKey);

            final RSAKey expected = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(alias)
                    .build();

            assertEquals(expected, this.subject.buildRsaKey(keyStore, alias, keyPassword));
        }
    }

    @Test
    void loadKeyStoreOk() {
        final InputStream inputStream = mock(InputStream.class);
        final String keyStorePassword = "keyStorePassword";

        try (final MockedStatic<RsaKeysLoader> utilities = mockStatic(RsaKeysLoader.class, CALLS_REAL_METHODS)) {
            final KeyStore expected = mock(KeyStore.class);
            utilities.when(RsaKeysLoader::keyStore).thenReturn(expected);

            utilities.when(() -> loadKeyStore(inputStream, expected, keyStorePassword)).thenAnswer(_ -> null);

            assertSame(expected, loadKeyStore(inputStream, keyStorePassword));
        }
    }

    @Test
    void keyStoreKo() {
        try (final MockedStatic<KeyStore> utilities = mockStatic(KeyStore.class, CALLS_REAL_METHODS)) {
            final KeyStoreException keyStoreException = mock(KeyStoreException.class);
            utilities.when(() -> KeyStore.getInstance(KEYSTORE_TYPE)).thenThrow(keyStoreException);

            final AuthServerTechnicalException exception = assertThrows(
                    AuthServerTechnicalException.class,
                    RsaKeysLoader::keyStore
            );

            assertSame(ERREUR_TECHNIQUE, exception.getCode());
            assertSame(ERREUR_INIT_KEY_STORE, exception.getMessage());
            assertSame(keyStoreException, exception.getCause());
        }
    }

    @Test
    void keyStoreOk() {
        try (final MockedStatic<KeyStore> utilities = mockStatic(KeyStore.class, CALLS_REAL_METHODS)) {
            final KeyStore keyStore = mock(KeyStore.class);
            utilities.when(() -> KeyStore.getInstance(KEYSTORE_TYPE)).thenReturn(keyStore);

            assertSame(keyStore, keyStore());
        }
    }

    @Test
    void loadKeyStoreIoExceptionKo() throws Exception {
        final InputStream inputStream = mock(InputStream.class);
        final KeyStore keyStore = mock(KeyStore.class);
        final String keyStorePassword = "keyStorePassword";

        final IOException ioException = new IOException();

        doThrow(ioException).when(keyStore).load(inputStream, keyStorePassword.toCharArray());

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> loadKeyStore(inputStream, keyStore, keyStorePassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertSame(ERREUR_LOADING_KEY_STORE, exception.getMessage());
        assertSame(ioException, exception.getCause());
    }

    @Test
    void loadKeyStoreNoSuchAlgorithmExceptionKo() throws Exception {
        final InputStream inputStream = mock(InputStream.class);
        final KeyStore keyStore = mock(KeyStore.class);
        final String keyStorePassword = "keyStorePassword";

        final NoSuchAlgorithmException exceptionCause = new NoSuchAlgorithmException();

        doThrow(exceptionCause).when(keyStore).load(inputStream, keyStorePassword.toCharArray());

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> loadKeyStore(inputStream, keyStore, keyStorePassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertSame(ERREUR_LOADING_KEY_STORE, exception.getMessage());
        assertSame(exceptionCause, exception.getCause());
    }

    @Test
    void loadKeyStoreCertificateExceptionKo() throws Exception {
        final InputStream inputStream = mock(InputStream.class);
        final KeyStore keyStore = mock(KeyStore.class);
        final String keyStorePassword = "keyStorePassword";

        final CertificateException exceptionCause = new CertificateException();

        doThrow(exceptionCause).when(keyStore).load(inputStream, keyStorePassword.toCharArray());

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> loadKeyStore(inputStream, keyStore, keyStorePassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertSame(ERREUR_LOADING_KEY_STORE, exception.getMessage());
        assertSame(exceptionCause, exception.getCause());
    }

    @Test
    void loadKeyStore2Ok() throws Exception {
        final InputStream inputStream = mock(InputStream.class);
        final KeyStore keyStore = mock(KeyStore.class);
        final String keyStorePassword = "keyStorePassword";

        loadKeyStore(inputStream, keyStore, keyStorePassword);

        verify(keyStore, times(1)).load(inputStream, keyStorePassword.toCharArray());
    }

    @Test
    void rsaPrivateKeyKeyStoreExceptionKo() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";
        final String keyPassword = "keyPassword";

        final KeyStoreException cause = new KeyStoreException();

        when(keyStore.getKey(alias, keyPassword.toCharArray())).thenThrow(cause);

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> rsaPrivateKey(keyStore, alias, keyPassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertEquals(
                ERREUR_RECUPERATION_CLE_PRIVEE.formatted(alias),
                exception.getMessage()
        );
        assertSame(cause, exception.getCause());
    }

    @Test
    void rsaPrivateKeyKeyStoreNoSuchAlgorithmExceptionKo() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";
        final String keyPassword = "keyPassword";

        final NoSuchAlgorithmException cause = new NoSuchAlgorithmException();

        when(keyStore.getKey(alias, keyPassword.toCharArray())).thenThrow(cause);

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> rsaPrivateKey(keyStore, alias, keyPassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertEquals(
                ERREUR_RECUPERATION_CLE_PRIVEE.formatted(alias),
                exception.getMessage()
        );
        assertSame(cause, exception.getCause());
    }

    @Test
    void rsaPrivateKeyKeyStoreUnrecoverableKeyExceptionKo() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";
        final String keyPassword = "keyPassword";

        final UnrecoverableKeyException cause = new UnrecoverableKeyException();

        when(keyStore.getKey(alias, keyPassword.toCharArray())).thenThrow(cause);

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> rsaPrivateKey(keyStore, alias, keyPassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertEquals(
                ERREUR_RECUPERATION_CLE_PRIVEE.formatted(alias),
                exception.getMessage()
        );
        assertSame(cause, exception.getCause());
    }

    @Test
    void rsaPrivateKeyNotFoundKo() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";
        final String keyPassword = "keyPassword";

        when(keyStore.getKey(alias, keyPassword.toCharArray())).thenReturn(null);

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> rsaPrivateKey(keyStore, alias, keyPassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertEquals(
                ERREUR_CLE_PRIVEE_INTROUVABLE.formatted(alias),
                exception.getMessage()
        );
    }

    @Test
    void rsaPrivateKeyBadFormatKo() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";
        final String keyPassword = "keyPassword";

        final Key privateKey = mock(PrivateKey.class);
        when(keyStore.getKey(alias, keyPassword.toCharArray())).thenReturn(privateKey);

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> rsaPrivateKey(keyStore, alias, keyPassword)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertEquals(
                ERREUR_PRIVATE_KEY_BAD_FORMAT.formatted(alias),
                exception.getMessage()
        );
    }

    @Test
    void rsaPrivateKeyOk() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";
        final String keyPassword = "keyPassword";

        final Key privateKey = mock(RSAPrivateKey.class);
        when(keyStore.getKey(alias, keyPassword.toCharArray())).thenReturn(privateKey);

        assertSame(privateKey, rsaPrivateKey(keyStore, alias, keyPassword));
    }

    @Test
    void rsaPublicKeyCertificateNotFoundKo() {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";

        try (final MockedStatic<RsaKeysLoader> utilities = mockStatic(RsaKeysLoader.class, CALLS_REAL_METHODS)) {
            utilities.when(() -> rsaPublicKeyCertificate(keyStore, alias)).thenReturn(null);

            final AuthServerTechnicalException exception = assertThrows(
                    AuthServerTechnicalException.class,
                    () -> rsaPublicKey(keyStore, alias)
            );

            assertSame(ERREUR_TECHNIQUE, exception.getCode());
            assertEquals(
                    ERREUR_CERTIFICATE_NOT_FOUND.formatted(alias),
                    exception.getMessage()
            );
        }
    }

    @Test
    void rsaPublicKeyBadFormatKo() {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";

        try (final MockedStatic<RsaKeysLoader> utilities = mockStatic(RsaKeysLoader.class, CALLS_REAL_METHODS)) {
            final Certificate certificate = mock(Certificate.class);
            utilities.when(() -> rsaPublicKeyCertificate(keyStore, alias)).thenReturn(certificate);

            final PublicKey publicKey = mock(PublicKey.class);
            when(certificate.getPublicKey()).thenReturn(publicKey);

            final AuthServerTechnicalException exception = assertThrows(
                    AuthServerTechnicalException.class,
                    () -> rsaPublicKey(keyStore, alias)
            );

            assertSame(ERREUR_TECHNIQUE, exception.getCode());
            assertEquals(
                    ERREUR_PUBLIC_KEY_BAD_FORMAT.formatted(alias),
                    exception.getMessage()
            );
        }
    }

    @Test
    void rsaPublicKeyOk() {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";

        try (final MockedStatic<RsaKeysLoader> utilities = mockStatic(RsaKeysLoader.class, CALLS_REAL_METHODS)) {
            final Certificate certificate = mock(Certificate.class);
            utilities.when(() -> rsaPublicKeyCertificate(keyStore, alias)).thenReturn(certificate);

            final PublicKey publicKey = mock(RSAPublicKey.class);
            when(certificate.getPublicKey()).thenReturn(publicKey);

            assertSame(publicKey, rsaPublicKey(keyStore, alias));
        }
    }

    @Test
    void rsaPublicKeyCertificateKo() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";

        final KeyStoreException cause = new KeyStoreException();

        when(keyStore.getCertificate(alias)).thenThrow(cause);

        final AuthServerTechnicalException exception = assertThrows(
                AuthServerTechnicalException.class,
                () -> rsaPublicKeyCertificate(keyStore, alias)
        );

        assertSame(ERREUR_TECHNIQUE, exception.getCode());
        assertEquals(
                ERREUR_GET_CERTIFICATE.formatted(alias),
                exception.getMessage()
        );
        assertSame(cause, exception.getCause());
    }

    @Test
    void rsaPublicKeyCertificateOk() throws Exception {
        final KeyStore keyStore = mock(KeyStore.class);
        final String alias = "alias";

        final Certificate certificate = mock(Certificate.class);

        when(keyStore.getCertificate(alias)).thenReturn(certificate);

        assertSame(certificate, rsaPublicKeyCertificate(
                keyStore,
                alias
        ));
    }

}
