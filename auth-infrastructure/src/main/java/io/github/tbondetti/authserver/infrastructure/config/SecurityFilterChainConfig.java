
package io.github.tbondetti.authserver.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityFilterChainConfig {

    static final String PATH_ACTUATOR_HEALTH = "/actuator/health";
    static final String PATH_ACTUATOR_INFO = "/actuator/info";

    private static final Customizer<SessionManagementConfigurer<HttpSecurity>> SESSION_MANAGEMENT_CUSTOMIZER = session -> session.sessionCreationPolicy(STATELESS);
    private static final Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> AUTHORIZED_HTTP_REQUESTS_CUSTOMIZER = auth -> auth
            .requestMatchers(PATH_ACTUATOR_HEALTH, PATH_ACTUATOR_INFO).permitAll()
            .anyRequest().authenticated();

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(SESSION_MANAGEMENT_CUSTOMIZER)
                .authorizeHttpRequests(AUTHORIZED_HTTP_REQUESTS_CUSTOMIZER)
                .httpBasic(withDefaults())
                .build();
    }
}
