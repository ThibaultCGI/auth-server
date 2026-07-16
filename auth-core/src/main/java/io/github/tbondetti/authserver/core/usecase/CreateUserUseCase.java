package io.github.tbondetti.authserver.core.usecase;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class CreateUserUseCase {

    static final int USERNAME_MAX_LENGTH = 100;
    static final int PASSWORD_MIN_LENGTH = 12;
    static final int PASSWORD_MAX_LENGTH = 128;

    static final String ERROR_USERNAME_IS_REQUIRED = "Le username est obligatoire.";
    static final String ERROR_USERNAME_TOO_LONG = "Le username ne doit pas dépasser les 100 caractères.";
    static final String ERROR_USERNAME_MUST_BE_UNIQUE = "Le username doit être unique.";

    static final String ERROR_PASSWORD_IS_REQUIRED = "Le mot de passe est obligatoire et ne peut pas être vide.";
    static final String ERROR_PASSWORD_TOO_SHORT = "Le mot de passe doit contenir au minimum 12 caractères.";
    static final String ERROR_PASSWORD_TOO_LONG = "Le mot de passe ne doit pas dépasser les 128 caractères.";

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public User execute(
            final String username,
            final String password
    ) {
        final String normalizedUsername = validateAndNormalizeUsername(username);

        validatePassword(password);
        this.ensureUsernameIsUnique(normalizedUsername);

        final String passwordHash = this.passwordEncoderPort.encode(password);

        final User user = User.builder()
                .id(randomUUID())
                .username(normalizedUsername)
                .passwordHash(passwordHash)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        return this.userRepositoryPort.save(user);
    }

    static String validateAndNormalizeUsername(final String username) {
        if (isNull(username) || username.isBlank()) {
            throw new AuthServerFunctionalException(ERROR_USERNAME_IS_REQUIRED);
        }

        final String normalizedUsername = normalizeUsername(username);

        if (normalizedUsername.length() > USERNAME_MAX_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_USERNAME_TOO_LONG);
        }

        return normalizedUsername;
    }

    static String normalizeUsername(final String username) {
        return username.trim().toLowerCase(ROOT);
    }

    static void validatePassword(final String password) {
        if (isNull(password) || password.isBlank()) {
            throw new AuthServerFunctionalException(ERROR_PASSWORD_IS_REQUIRED);
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_PASSWORD_TOO_SHORT);
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_PASSWORD_TOO_LONG);
        }
    }

    void ensureUsernameIsUnique(final String normalizedUsername) {
        final Optional<User> optionalUser = this.userRepositoryPort.findByUsername(normalizedUsername);

        if (optionalUser.isPresent()) {
            throw new AuthServerFunctionalException(ERROR_USERNAME_MUST_BE_UNIQUE);
        }
    }
}
