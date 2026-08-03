package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ERREUR_TECHNIQUE;
import static java.security.KeyPairGenerator.getInstance;
import static java.util.UUID.randomUUID;

@Configuration
@RequiredArgsConstructor
public class OAuth2AuthorizationServerConfiguration {

    static final String ALGORITHM_RSA = "RSA";

    private final RegisteredClientRepository registeredClientRepository;

    @SuppressWarnings("java:S4502")
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(final HttpSecurity http) {
        final OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        return http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, authorizationServer -> authorizationServer.registeredClientRepository(this.registeredClientRepository))
                .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.getEndpointsMatcher()))
                .cors(Customizer.withDefaults()) // pas nécessaire CorsFilter seul suffit
                .build();
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        final RSAKey rsaKey = generateRsaKey();
        final JWKSet jwkSet = new JWKSet(rsaKey);

        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static RSAKey generateRsaKey() {
        try {
            final KeyPairGenerator keyPairGenerator = getInstance(ALGORITHM_RSA);
            keyPairGenerator.initialize(2048);

            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

            final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(randomUUID().toString())
                    .build();
        } catch (final NoSuchAlgorithmException e) {
            throw new AuthServerTechnicalException(
                    ERREUR_TECHNIQUE,
                    "Erreur lors de la génération de la clé RSA",
                    e
            );
        }
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> oauth2JwtCustomizer(final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort) {
        return new OAuth2JwtCustomizer(oauth2ClientRepositoryPort);
    }
}