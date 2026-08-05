
package io.github.tbondetti.authserver.web.security;

import io.github.tbondetti.authserver.web.security.handler.ApiAccessDeniedHandler;
import io.github.tbondetti.authserver.web.security.handler.ApiAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static io.github.tbondetti.authserver.web.security.SecurityPaths.ACTUATOR_ALL;
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


    @SuppressWarnings("java:S4502")
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        return http
                // CSRF désactivé : API stateless, pas de session navigateur.
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Pas d'authentification requise pour les endpoint suivants
                        .requestMatchers(
                                ACTUATOR_ALL,
                                SWAGGER_UI_ALL,
                                SWAGGER_UI_HTML,
                                OPENAPI_DOCS_ALL,
                                OPENAPI_DOCS_YAML
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .httpBasic(withDefaults())

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(this.apiAuthenticationEntryPoint)
                        .accessDeniedHandler(this.apiAccessDeniedHandler)
                )

                .build();
    }
}
