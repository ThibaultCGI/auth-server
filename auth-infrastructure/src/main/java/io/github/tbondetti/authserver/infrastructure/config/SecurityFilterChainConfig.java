
package io.github.tbondetti.authserver.infrastructure.config;

import io.github.tbondetti.authserver.infrastructure.security.ApiAccessDeniedHandler;
import io.github.tbondetti.authserver.infrastructure.security.ApiAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfig {

    private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;
    private final ApiAccessDeniedHandler apiAccessDeniedHandler;

    static final String PATH_ACTUATOR_HEALTH = "/actuator/health";
    static final String PATH_ACTUATOR_INFO = "/actuator/info";

    static final Customizer<SessionManagementConfigurer<HttpSecurity>> SESSION_MANAGEMENT_CUSTOMIZER = session -> session.sessionCreationPolicy(STATELESS);
    static final Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> AUTHORIZED_HTTP_REQUESTS_CUSTOMIZER = auth -> auth
//            .anyRequest().permitAll();
            .requestMatchers(PATH_ACTUATOR_HEALTH, PATH_ACTUATOR_INFO).permitAll()
            .anyRequest().authenticated();

    @SuppressWarnings("java:S4502")
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        return http
                // CSRF désactivé : API stateless, pas de session navigateur.
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(SESSION_MANAGEMENT_CUSTOMIZER)
                .authorizeHttpRequests(AUTHORIZED_HTTP_REQUESTS_CUSTOMIZER)
                .httpBasic(withDefaults())
                .exceptionHandling(this.exceptionHandling())
                .build();
    }

    protected Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandling() {
        return exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(this.apiAuthenticationEntryPoint)
                .accessDeniedHandler(this.apiAccessDeniedHandler);
    }
}
