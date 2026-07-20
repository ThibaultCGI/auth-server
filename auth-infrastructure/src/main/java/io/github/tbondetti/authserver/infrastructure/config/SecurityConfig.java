package io.github.tbondetti.authserver.infrastructure.config;


import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.usecase.user.GetAllUserRolesUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import io.github.tbondetti.authserver.infrastructure.security.AuthServerUserDetailsService;
import io.github.tbondetti.authserver.infrastructure.security.PasswordEncoderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoderPort passwordEncoderPort(final PasswordEncoder passwordEncoder) {
        return new PasswordEncoderAdapter(passwordEncoder);
    }

    @Bean
    public UserDetailsService userDetailsService(
            final GetUserUseCase getUserUseCase,
            final GetAllUserRolesUseCase getAllUserRolesUseCase
    ) {
        return new AuthServerUserDetailsService(getUserUseCase, getAllUserRolesUseCase);
    }
}
