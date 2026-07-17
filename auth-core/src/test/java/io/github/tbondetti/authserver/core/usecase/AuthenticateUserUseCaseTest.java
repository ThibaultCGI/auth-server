package io.github.tbondetti.authserver.core.usecase;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.tbondetti.authserver.core.constants.TestConstants.GIVEN_PASSWORD;
import static io.github.tbondetti.authserver.core.constants.TestConstants.GIVEN_USER_NAME;
import static io.github.tbondetti.authserver.core.usecase.AuthenticateUserUseCase.ERROR_ACCOUNT_DISABLED;
import static io.github.tbondetti.authserver.core.usecase.AuthenticateUserUseCase.ERROR_INVALID_CREDENTIALS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @InjectMocks
    private AuthenticateUserUseCase subject;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private GetUserUseCase getUserUseCase;

    @Test
    void shouldCredentialsBeValid() {
        final String passwordHash = "passwordHash";
        final User user = User.builder()
                .passwordHash(passwordHash)
                .enabled(true)
                .build();

        when(this.getUserUseCase.execute(GIVEN_USER_NAME)).thenReturn(user);
        when(this.passwordEncoderPort.matches(GIVEN_PASSWORD, passwordHash)).thenReturn(false);

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.execute(GIVEN_USER_NAME, GIVEN_PASSWORD)
        );

        assertEquals(ERROR_INVALID_CREDENTIALS, exception.getMessage());
    }

    @Test
    void shouldUserBeEnabled() {
        final String passwordHash = "passwordHash";
        final User user = User.builder()
                .passwordHash(passwordHash)
                .enabled(false)
                .build();

        when(this.getUserUseCase.execute(GIVEN_USER_NAME)).thenReturn(user);
        when(this.passwordEncoderPort.matches(GIVEN_PASSWORD, passwordHash)).thenReturn(true);

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.execute(GIVEN_USER_NAME, GIVEN_PASSWORD)
        );

        assertEquals(ERROR_ACCOUNT_DISABLED, exception.getMessage());
    }

    @Test
    void shouldReturnUser() {
        final String passwordHash = "passwordHash";
        final User user = User.builder()
                .passwordHash(passwordHash)
                .enabled(true)
                .build();

        when(this.getUserUseCase.execute(GIVEN_USER_NAME)).thenReturn(user);
        when(this.passwordEncoderPort.matches(GIVEN_PASSWORD, passwordHash)).thenReturn(true);

        assertSame(user, this.subject.execute(GIVEN_USER_NAME, GIVEN_PASSWORD));
    }
}