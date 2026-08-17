package io.github.tbondetti.authserver.security.oauth2;

import com.nimbusds.jose.jwk.RSAKey;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import io.github.tbondetti.authserver.security.properties.JwtKeyStoreProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RsaKeysLoaderTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private JwtKeyStoreProperties jwtKeyStoreProperties;

    @Test
    void should_return_keystore() {

        final KeyStore keyStore = RsaKeysLoader.keyStore();

        assertThat(keyStore)
                .isNotNull();

        assertThat(keyStore.getType())
                .isEqualTo(RsaKeysLoader.KEYSTORE_TYPE);
    }

    @Test
    void should_throw_exception_when_private_key_not_found() {

        final KeyStore keyStore = mock(KeyStore.class);

        assertThatThrownBy(() ->
                RsaKeysLoader.rsaPrivateKey(
                        keyStore,
                        "alias",
                        "password"
                )
        )
                .isInstanceOf(AuthServerTechnicalException.class)
                .hasMessageContaining("Clé privée alias introuvable");
    }

    @Test
    void should_throw_exception_when_private_key_is_not_rsa() throws Exception {

        final KeyStore keyStore = mock(KeyStore.class);

        when(keyStore.getKey("alias", "password".toCharArray()))
                .thenReturn(mock(Key.class));

        assertThatThrownBy(() ->
                RsaKeysLoader.rsaPrivateKey(
                        keyStore,
                        "alias",
                        "password"
                )
        )
                .isInstanceOf(AuthServerTechnicalException.class)
                .hasMessageContaining("Format de clé privée alias incorrect");
    }

    @Test
    void should_return_rsa_private_key() throws Exception {

        final KeyStore keyStore = mock(KeyStore.class);
        final RSAPrivateKey privateKey = mock(RSAPrivateKey.class);

        when(keyStore.getKey("alias", "password".toCharArray()))
                .thenReturn(privateKey);

        final RSAPrivateKey result =
                RsaKeysLoader.rsaPrivateKey(
                        keyStore,
                        "alias",
                        "password"
                );

        assertThat(result)
                .isSameAs(privateKey);
    }

    @Test
    void should_throw_exception_when_certificate_not_found() {

        final KeyStore keyStore = mock(KeyStore.class);

        assertThatThrownBy(() ->
                RsaKeysLoader.rsaPublicKey(
                        keyStore,
                        "alias"
                )
        )
                .isInstanceOf(AuthServerTechnicalException.class)
                .hasMessageContaining("Certificat introuvable pour la clé alias");
    }

    @Test
    void should_throw_exception_when_public_key_is_not_rsa() throws Exception {

        final KeyStore keyStore = mock(KeyStore.class);

        final Certificate certificate = mock(Certificate.class);
        final PublicKey publicKey = mock(PublicKey.class);

        when(keyStore.getCertificate("alias"))
                .thenReturn(certificate);

        when(certificate.getPublicKey())
                .thenReturn(publicKey);

        assertThatThrownBy(() ->
                RsaKeysLoader.rsaPublicKey(
                        keyStore,
                        "alias"
                )
        )
                .isInstanceOf(AuthServerTechnicalException.class)
                .hasMessageContaining("Format de clé publique alias incorrect");
    }

    @Test
    void should_return_rsa_public_key() throws Exception {

        final KeyStore keyStore = mock(KeyStore.class);

        final Certificate certificate = mock(Certificate.class);
        final RSAPublicKey publicKey = mock(RSAPublicKey.class);

        when(keyStore.getCertificate("alias"))
                .thenReturn(certificate);

        when(certificate.getPublicKey())
                .thenReturn(publicKey);

        final RSAPublicKey result =
                RsaKeysLoader.rsaPublicKey(
                        keyStore,
                        "alias"
                );

        assertThat(result)
                .isSameAs(publicKey);
    }

    @Test
    void should_build_rsa_key() throws Exception {

        final KeyStore keyStore = mock(KeyStore.class);

        final KeyPairGenerator generator =
                KeyPairGenerator.getInstance("RSA");

        generator.initialize(2048);

        final KeyPair keyPair =
                generator.generateKeyPair();

        final RSAPublicKey publicKey =
                (RSAPublicKey) keyPair.getPublic();

        final RSAPrivateKey privateKey =
                (RSAPrivateKey) keyPair.getPrivate();

        final Certificate certificate = mock(Certificate.class);

        when(keyStore.getKey("alias", "password".toCharArray()))
                .thenReturn(privateKey);

        when(keyStore.getCertificate("alias"))
                .thenReturn(certificate);

        when(certificate.getPublicKey())
                .thenReturn(publicKey);

        final RSAKey rsaKey =
                RsaKeysLoader.buildRsaKey(
                        keyStore,
                        "alias",
                        "password"
                );

        assertThat(rsaKey.getKeyID())
                .isEqualTo("alias");

        assertThat(rsaKey.toRSAPublicKey())
                .isEqualTo(publicKey);
    }

    @Test
    void should_throw_exception_when_resource_cannot_be_loaded() throws IOException {

        final Resource resource = mock(Resource.class);

        when(jwtKeyStoreProperties.location())
                .thenReturn("classpath:test.p12");

        when(resourceLoader.getResource("classpath:test.p12"))
                .thenReturn(resource);

        when(resource.getInputStream())
                .thenThrow(new IOException());

        final RsaKeysLoader loader =
                new RsaKeysLoader(
                        resourceLoader,
                        jwtKeyStoreProperties
                );

        assertThatThrownBy(loader::loadAll)
                .isInstanceOf(AuthServerTechnicalException.class)
                .hasMessageContaining(
                        RsaKeysLoader.ERREUR_CHARGEMENT_CLE_RSA
                );
    }

}
