package io.github.tbondetti.authserver.core.usecase;

import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.usecase.user.GetUserUseCase;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ACCOUNT_DISABLED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.INVALID_CREDENTIALS;

@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    static final String ERROR_INVALID_CREDENTIALS = "Identifiants invalides.";
    static final String ERROR_ACCOUNT_DISABLED = "Le compte utilisateur est désactivé.";

    private final PasswordEncoderPort passwordEncoderPort;
    private final GetUserUseCase getUserUseCase;

    public User execute(
            final String username,
            final String rawPassword
    ) {
        final User user = this.getUserUseCase.execute(username);

        if (!this.passwordEncoderPort.matches(rawPassword, user.passwordHash())) {
            throw new AuthServerFunctionalException(INVALID_CREDENTIALS, ERROR_INVALID_CREDENTIALS);
        }

        if (!user.enabled()) {
            throw new AuthServerFunctionalException(ACCOUNT_DISABLED, ERROR_ACCOUNT_DISABLED);
        }

        return user;
    }
}
