package io.github.tbondetti.authserver.core.usecase;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.validateAndNormalizeUsername;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.validatePassword;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class CreateUserUseCase {
    static final String ERROR_USERNAME_MUST_BE_UNIQUE = "Le username doit être unique.";

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
                .build();

        return this.userRepositoryPort.save(user);
    }

    void ensureUsernameIsUnique(final String normalizedUsername) {
        final Optional<User> optionalUser = this.userRepositoryPort.findByUsername(normalizedUsername);

        if (optionalUser.isPresent()) {
            throw new AuthServerFunctionalException(ERROR_USERNAME_MUST_BE_UNIQUE);
        }
    }
}
