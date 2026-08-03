package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;

/**
 * Configuration CORS permettant à
 * Swagger UI du Test Resource Server
 * d'obtenir un token OAuth2
 * depuis l'Authorization Server.
 */
@Configuration
public class OAuth2CorsConfiguration {

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    private CorsConfiguration corsConfiguration() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(this.allowedOrigins);
        configuration.setAllowedMethods(List.of(
                GET.name(),
                POST.name(),
                OPTIONS.name()
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        return configuration;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", this.corsConfiguration());

        return source;
    }

    /*
    Permet la gestion des requêtes CORS préflight vers les endpoints OAuth2 (/oauth2/token).
    Sans ce CorsFilter explicite, Swagger UI ne parvenait pas à récupérer un token OAuth2 depuis une origine différente
    (http://localhost:8081 → http://localhost:8080).
    */
    @Bean
    public CorsFilter corsFilter(final CorsConfigurationSource corsConfigurationSource) {
        return new CorsFilter(corsConfigurationSource);
    }

}