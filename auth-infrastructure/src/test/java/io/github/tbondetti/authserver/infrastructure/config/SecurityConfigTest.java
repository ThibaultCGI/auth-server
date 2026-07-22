package io.github.tbondetti.authserver.infrastructure.config;

import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.usecase.user.GetAllUserRolesUseCase;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import io.github.tbondetti.authserver.infrastructure.security.AuthServerUserDetailsService;
import io.github.tbondetti.authserver.infrastructure.security.PasswordEncoderAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig subject;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private GetAllUserRolesUseCase getAllUserRolesUseCase;

    @Test
    void passwordEncoderShouldReturnPasswordEncoder() {
        final PasswordEncoder actual = this.subject.passwordEncoder();

        assertNotNull(actual);
        assertInstanceOf(BCryptPasswordEncoder.class, actual);
    }

    @Test
    void passwordEncoderPortShouldReturnPasswordEncoderPort() {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        final PasswordEncoderPort actual = this.subject.passwordEncoderPort(passwordEncoder);

        assertNotNull(actual);
        assertInstanceOf(PasswordEncoderAdapter.class, actual);
    }

    @Test
    void userDetailsServiceShouldReturnUserDetailsService() {
        final UserDetailsService actual = this.subject.userDetailsService(
                this.getUserUseCase,
                this.getAllUserRolesUseCase
        );

        assertNotNull(actual);
        assertInstanceOf(AuthServerUserDetailsService.class, actual);    }
}