package io.github.tbondetti.authserver.security.oauth2;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.tbondetti.authserver.core.exception.AuthServerTechnicalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.security.properties.JwtKeyStoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.List;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ERREUR_TECHNIQUE;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtKeyStoreProperties.class)
public class OAuth2AuthorizationServerConfiguration {

    static final String ERREUR_NO_ACTIVE_KEY = "Aucune clé active n'a été trouvée dans le key store.";

    private final RegisteredClientRepository registeredClientRepository;
    private final JwtKeyStoreProperties jwtKeyStoreProperties;
    private final RsaKeysLoader rsaKeysLoader;


    @SuppressWarnings("java:S4502")
    @Bean
    @Order(1)
    // Déclare la SecurityFilterChain prioritaire (Order 1) qui protège tous les endpoints OAuth2 de l'Authorization Server.
    public SecurityFilterChain authorizationServerSecurityFilterChain(final HttpSecurity http) {

        // Crée le composant Spring Authorization Server.
        // C'est lui qui ajoute les endpoints OAuth2 :
        // - /oauth2/authorize
        // - /oauth2/token
        // - /oauth2/jwks
        // - /.well-known/oauth-authorization-server
        final OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        return http
                // Cette SecurityFilterChain ne s'applique qu'aux endpoints OAuth2 de l'Authorization Server.
                // → Les autres URL tomberont dans les autres SecurityFilterChain.
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())

                // Toutes les requêtes OAuth2 doivent être authentifiées.
                // → Pour /oauth2/authorize cela signifie : l'utilisateur doit être connecté.
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())

                // Si une requête OAuth2 arrive sans utilisateur authentifié, au lieu de retourner une erreur,
                // Spring redirige automatiquement vers /login.
                // C'est normalement cette configuration qui doit résoudre le problème du "principal".

                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))

                // Autorise la création d'une session HTTP si Spring en a besoin.
                // L'Authorization Code Flow nécessite une session pour conserver l'authentification utilisateur.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // Branche le RegisteredClientRepository.
                // À chaque requête OAuth2, Spring récupère les informations du client, à savoir
                // → client_id
                // → scopes
                // → redirect_uri
                // → grant types
                // → PKCE
                .with(authorizationServerConfigurer, authorizationServer -> authorizationServer
                        .registeredClientRepository(this.registeredClientRepository)
                        .oidc(Customizer.withDefaults())
                )

                // Ignore les protections CSRF pour les endpoints OAuth2.
                // Exemple : `POST /oauth2/token` ne nécessite pas de token CSRF.
                .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.getEndpointsMatcher()))

                // Active le support CORS.
                // Utile pour les futures applications SPA (Angular, React, Vue) et les Swagger UI
                .cors(Customizer.withDefaults())

                // Construit et retourne la SecurityFilterChain OAuth2.
                .build();

    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        final List<JWK> keys = this.rsaKeysLoader.loadAll().stream().map(JWK.class::cast).toList();
        final JWKSet jwkSet = new JWKSet(keys);

        return (selector, _) -> selector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(
            final JWKSource<SecurityContext> jwkSource
    ) {
        final NimbusJwtEncoder encoder = new NimbusJwtEncoder(jwkSource);

        encoder.setJwkSelector(jwks -> jwks.stream()
                .filter(jwk -> this.jwtKeyStoreProperties.isActive(jwk.getKeyID()))
                .findFirst()
                .orElseThrow(() -> new AuthServerTechnicalException(ERREUR_TECHNIQUE, ERREUR_NO_ACTIVE_KEY))
        );

        return encoder;
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> oauth2JwtCustomizer(final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort) {
        return new OAuth2JwtCustomizer(oauth2ClientRepositoryPort);
    }
}