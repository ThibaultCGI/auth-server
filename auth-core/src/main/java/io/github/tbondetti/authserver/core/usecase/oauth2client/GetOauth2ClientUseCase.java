package io.github.tbondetti.authserver.core.usecase.oauth2client;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeClientId;

@RequiredArgsConstructor
public class GetOauth2ClientUseCase {
    static final String ERROR_CLIENT_NOT_FOUND = "Aucun client avec client-id %s n'est présent dans le référentiel.";

    private final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    public OAuth2Client execute(final String clientId) {
        final String normalizedClientId = normalizeClientId(clientId);

        return this.oauth2ClientRepositoryPort.findByClientId(normalizedClientId).orElseThrow(
                () -> new AuthServerFunctionalException(
                        CLIENT_NOT_FOUND,
                        ERROR_CLIENT_NOT_FOUND.formatted(normalizedClientId)
                )
        );
    }
}
