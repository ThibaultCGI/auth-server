package io.github.tbondetti.authserver.core.usecase.oauth2client;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_ID_ALREADY_EXISTS;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientId;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientName;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateClientSecret;

@RequiredArgsConstructor
public class CreateOAuth2ClientUseCase {
    static final String ERROR_CLIENT_ID_MUST_BE_UNIQUE = "Le client id doit être unique.";

    private final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public OAuth2Client execute(
            final String clientId,
            final String clientName,
            final String clientSecret,
            final String applicationCode
    ) {
        final String normalizedClientId = validateAndNormalizeClientId(clientId);
        this.ensureClientIdIsUnique(normalizedClientId);

        final String normalizedClientName = validateAndNormalizeClientName(clientName);
        validateClientSecret(clientSecret);
        final Application application = this.getApplicationUseCase.execute(applicationCode);

        final String clientSecretHash = this.passwordEncoderPort.encode(clientSecret);

        final OAuth2Client client = OAuth2Client.builder()
                .clientId(normalizedClientId)
                .clientName(normalizedClientName)
                .clientSecretHash(clientSecretHash)
                .applicationCode(application.code())
                .build();

        return this.oauth2ClientRepositoryPort.save(client);
    }

    void ensureClientIdIsUnique(final String normalizedClientId) {
        final Optional<OAuth2Client> optionalClient = this.oauth2ClientRepositoryPort.findByClientId(normalizedClientId);

        if (optionalClient.isPresent()) {
            throw new AuthServerFunctionalException(CLIENT_ID_ALREADY_EXISTS, ERROR_CLIENT_ID_MUST_BE_UNIQUE);
        }
    }
}
