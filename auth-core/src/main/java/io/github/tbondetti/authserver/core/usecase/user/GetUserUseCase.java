package io.github.tbondetti.authserver.core.usecase.user;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.USER_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.normalizeUsername;

@RequiredArgsConstructor
public class GetUserUseCase {
    static final String ERROR_USER_NOT_FOUND = "Aucun utilisateur avec username %s n'est présent dans le référentiel.";

    private final UserRepositoryPort userRepositoryPort;

    public User execute(final String username) {
        final String normalizedUsername = normalizeUsername(username);

        return this.userRepositoryPort.findByUsername(normalizedUsername).orElseThrow(
                () -> new AuthServerFunctionalException(
                        USER_NOT_FOUND,
                        ERROR_USER_NOT_FOUND.formatted(normalizedUsername))
        );
    }
}
