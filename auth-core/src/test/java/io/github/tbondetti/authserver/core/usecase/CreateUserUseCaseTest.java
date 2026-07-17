package io.github.tbondetti.authserver.core.usecase;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.core.utils.UserValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.constants.TestConstants.USER_NAME;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.ERROR_USERNAME_MUST_BE_UNIQUE;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.validateAndNormalizeUsername;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.validatePassword;
import static java.time.LocalDateTime.now;
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Spy
    @InjectMocks
    private CreateUserUseCase subject;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private Clock clock;


    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExistsDuringUsernameUniquenessCheck() {
        final User existingUser = User.builder().build();

        when(this.userRepositoryPort.findByUsername(USER_NAME))
                .thenReturn(Optional.of(existingUser));

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.ensureUsernameIsUnique(USER_NAME)
        );

        assertEquals(ERROR_USERNAME_MUST_BE_UNIQUE, exception.getMessage());
    }

    @Test
    void shouldReturnNormallyWhenUsernameDoesNotExist() {
        when(this.userRepositoryPort.findByUsername(USER_NAME))
                .thenReturn(Optional.empty());

        this.subject.ensureUsernameIsUnique(USER_NAME);

        verify(this.userRepositoryPort, times(1)).findByUsername(USER_NAME);
    }

    @Test
    void executeOk() {
        final String givenPassword = "givenPassword";
        final String givenUsername = "  " + USER_NAME + "  ";

        final String normalizedUsername = USER_NAME.toLowerCase(ROOT);
        doNothing().when(this.subject).ensureUsernameIsUnique(normalizedUsername);

        final String passwordHash = "passwordHash";
        when(this.passwordEncoderPort.encode(givenPassword)).thenReturn(passwordHash);

        final UUID expectedId = UUID.randomUUID();
        final LocalDateTime expectedCreatedAt = now();

        try (final MockedStatic<UserValidationUtils> userUtilities = mockStatic(UserValidationUtils.class);
             final MockedStatic<UUID> uUIDUtilities = mockStatic(UUID.class);
             final MockedStatic<LocalDateTime> localDateTimeUtilities = mockStatic(LocalDateTime.class)
        ) {
            userUtilities.when(() -> validateAndNormalizeUsername(givenUsername)).thenReturn(normalizedUsername);
            uUIDUtilities.when(UUID::randomUUID).thenReturn(expectedId);
            localDateTimeUtilities.when(() -> now(this.clock)).thenReturn(expectedCreatedAt);

            final User expected = User.builder()
                    .id(expectedId)
                    .username(normalizedUsername)
                    .passwordHash(passwordHash)
                    .enabled(true)
                    .createdAt(expectedCreatedAt)
                    .build();

            when(this.userRepositoryPort.save(expected)).thenReturn(expected);

            assertSame(expected, this.subject.execute(givenUsername, givenPassword));

            userUtilities.verify(
                    () -> validateAndNormalizeUsername(givenUsername),
                    times(1)
            );

            userUtilities.verify(
                    () -> validatePassword(givenPassword),
                    times(1)
            );

            verify(this.subject, times(1)).ensureUsernameIsUnique(normalizedUsername);
        }
    }
}
