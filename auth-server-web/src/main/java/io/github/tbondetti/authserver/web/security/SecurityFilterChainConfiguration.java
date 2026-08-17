package io.github.tbondetti.authserver.web.security;

import io.github.tbondetti.authserver.web.security.handler.ApiAccessDeniedHandler;
import io.github.tbondetti.authserver.web.security.handler.ApiAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static io.github.tbondetti.authserver.web.security.SecurityPaths.ACTUATOR_ALL;
import static io.github.tbondetti.authserver.web.security.SecurityPaths.DEFAULT_UI;
import static io.github.tbondetti.authserver.web.security.SecurityPaths.ERROR;
import static io.github.tbondetti.authserver.web.security.SecurityPaths.FAVICON;
import static io.github.tbondetti.authserver.web.security.SecurityPaths.OPENAPI_DOCS_ALL;
import static io.github.tbondetti.authserver.web.security.SecurityPaths.OPENAPI_DOCS_YAML;
import static io.github.tbondetti.authserver.web.security.SecurityPaths.SWAGGER_UI_ALL;
import static io.github.tbondetti.authserver.web.security.SecurityPaths.SWAGGER_UI_HTML;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfiguration {

    private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;
    private final ApiAccessDeniedHandler apiAccessDeniedHandler;


    @Bean
    @Order(2)
    // Déclare une SecurityFilterChain dédiée à la page de login.
    // Elle est exécutée après la chaîne OAuth2 (Order 1) mais avant la chaîne API (Order 3).
    public SecurityFilterChain formLoginSecurityFilterChain(final HttpSecurity http) {

        return http
                // Cette chaîne ne traite que l'URL /login.
                // Exemple : `GET /login` et `POST /login`
                // Les autres URL sont ignorées et seront traitées par d'autres SecurityFilterChain.
                .securityMatcher("/login")

                // Autorise tout le monde à accéder à /login.
                // Un utilisateur anonyme doit pouvoir afficher la page de connexion sans être déjà authentifié.
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                // Active le système de login HTML intégré à Spring Security.
                // Ajoute automatiquement :
                // - `GET /login` → affiche un formulaire HTML
                // - `POST /login` → traite l'authentification
                // - Logout
                // - Gestion des erreurs
                // - Session utilisateur
                .formLogin(Customizer.withDefaults())

                // Construit et retourne la SecurityFilterChain.
                .build();
    }


    @SuppressWarnings("java:S4502")
    @Bean
    @Order(3)
    // SecurityFilterChain générique. Elle est exécutée uniquement si aucune autre SecurityFilterChain plus prioritaire n'a matché la requête.
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        return http
                // Désactive totalement CSRF. Adapté aux APIs REST stateless.
                .csrf(AbstractHttpConfigurer::disable)

                // Interdit les sessions HTTP. Chaque requête doit contenir ses informations d'authentification.
                // Typiquement : Basic Auth ou Bearer Token.
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                // Règles d'autorisation pour les endpoints non OAuth2.
                .authorizeHttpRequests(auth -> auth

                        // Ces endpoints sont publics. Aucun utilisateur connecté n'est requis.
                        .requestMatchers(
                                ACTUATOR_ALL,
                                SWAGGER_UI_ALL,
                                SWAGGER_UI_HTML,
                                OPENAPI_DOCS_ALL,
                                OPENAPI_DOCS_YAML,
                                FAVICON,
                                ERROR,
                                DEFAULT_UI
                        ).permitAll()

                        // Toutes les autres URL nécessitent une authentification.
                        .anyRequest().authenticated()
                )

                // Active HTTP Basic Authentication.
                // Exemple : Authorization: Basic xxxxx utilisé par Bruno ou Postman.
                .httpBasic(withDefaults())

                // Gestion personnalisée des erreurs sécurité.
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // Appelé lorsqu'un utilisateur tente d'accéder à une ressource sans être authentifié.
                        // Retourne le JSON :
                        // `{ "code":"INVALID_CREDENTIALS", description: "Nom d'utilisateur ou mot de passe incorrect" }`
                        .authenticationEntryPoint(this.apiAuthenticationEntryPoint)

                        // Appelé lorsque l'utilisateur est authentifié mais n'a pas les droits suffisants.
                        // Typiquement : 403 Forbidden.
                        .accessDeniedHandler(this.apiAccessDeniedHandler)
                )

                .build();
    }
}
