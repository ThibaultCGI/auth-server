package io.github.tbondetti.authserver.core.usecase;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.constants.TestConstants.TEN_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.TestConstants.USER_NAME;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.ERROR_PASSWORD_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.ERROR_PASSWORD_TOO_LONG;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.ERROR_PASSWORD_TOO_SHORT;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.ERROR_USERNAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.ERROR_USERNAME_MUST_BE_UNIQUE;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.ERROR_USERNAME_TOO_LONG;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.normalizeUsername;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.validateAndNormalizeUsername;
import static io.github.tbondetti.authserver.core.usecase.CreateUserUseCase.validatePassword;
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
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
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword(null)
        );

        assertEquals(ERROR_PASSWORD_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword("   ")
        );

        assertEquals(ERROR_PASSWORD_IS_REQUIRED, exception2.getMessage());

        final AuthServerFunctionalException exception3 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword("12345678901")
        );

        assertEquals(ERROR_PASSWORD_TOO_SHORT, exception3.getMessage());

        final AuthServerFunctionalException exception4 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword(
                        TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                "123456789"
                )
        );

        assertEquals(ERROR_PASSWORD_TOO_LONG, exception4.getMessage());
    }

    @Test
    void normalizeUsernameOk() {
        assertEquals("user_name", normalizeUsername("  " + USER_NAME + "  "));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsInvalid() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeUsername(null)
        );

        assertEquals(ERROR_USERNAME_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeUsername("   ")
        );

        assertEquals(ERROR_USERNAME_IS_REQUIRED, exception2.getMessage());

        final String string101 = TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                "1";

        try (MockedStatic<CreateUserUseCase> utilities = mockStatic(CreateUserUseCase.class, CALLS_REAL_METHODS)) {
            utilities.when(() -> normalizeUsername(USER_NAME)).thenReturn(string101); // déjà testé

            final AuthServerFunctionalException exception3 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeUsername(USER_NAME)
            );

            assertEquals(ERROR_USERNAME_TOO_LONG, exception3.getMessage());
        }
    }

    @Test
    void shouldNormalizeUsernameWhenUsernameIsValid() {
        assertEquals(USER_NAME.toLowerCase(ROOT), validateAndNormalizeUsername("   " + USER_NAME + "  "));
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
        final LocalDateTime expectedCreatedAt = LocalDateTime.now();

        try (final MockedStatic<CreateUserUseCase> createUserUseCaseUtilities = mockStatic(CreateUserUseCase.class);
             final MockedStatic<UUID> uUIDUtilities = mockStatic(UUID.class);
             final MockedStatic<LocalDateTime> localDateTimeUtilities = mockStatic(LocalDateTime.class)
        ) {
            createUserUseCaseUtilities.when(() -> validateAndNormalizeUsername(givenUsername)).thenReturn(normalizedUsername);
            uUIDUtilities.when(UUID::randomUUID).thenReturn(expectedId);
            localDateTimeUtilities.when(LocalDateTime::now).thenReturn(expectedCreatedAt);

            final User expected = User.builder()
                    .id(expectedId)
                    .username(normalizedUsername)
                    .passwordHash(passwordHash)
                    .enabled(true)
                    .createdAt(expectedCreatedAt)
                    .build();

            when(this.userRepositoryPort.save(expected)).thenReturn(expected);

            assertSame(expected, this.subject.execute(givenUsername, givenPassword));

            createUserUseCaseUtilities.verify(
                    () -> validateAndNormalizeUsername(givenUsername),
                    times(1)
            );

            createUserUseCaseUtilities.verify(
                    () -> validatePassword(givenPassword),
                    times(1)
            );

            verify(this.subject, times(1)).ensureUsernameIsUnique(normalizedUsername);
        }
    }
}
