package io.github.tbondetti.authserver.core.usecase.oauth2client;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientCredentialsGeneratorPort;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_ID_GENERATION_FAILED;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientName;

@RequiredArgsConstructor
public class CreateOAuth2ClientUseCase {

    static final int MAX_CLIENT_ID_GENERATION_ATTEMPTS = 5;
    static final String ERROR_CLIENT_ID_GENERATION_FAILED = "Impossible de générer un client ID unique après plusieurs tentatives.";

    private final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;
    private final OAuth2ClientCredentialsGeneratorPort oauth2ClientCredentialsGeneratorPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public OAuth2CreatedClient execute(
            final String clientName,
            final String applicationCode
    ) {
        final String normalizedClientName = validateAndNormalizeClientName(clientName);
        final Application application = this.getApplicationUseCase.execute(applicationCode);
        final String clientId = this.generateClientId();
        final String clientSecret = this.oauth2ClientCredentialsGeneratorPort.generateClientSecret();

        final String clientSecretHash = this.passwordEncoderPort.encode(clientSecret);

        final OAuth2Client clientToSave = OAuth2Client.builder()
                .clientId(clientId)
                .clientName(normalizedClientName)
                .clientSecretHash(clientSecretHash)
                .applicationCode(application.code())
                .build();

        final OAuth2Client clientSaved = this.oauth2ClientRepositoryPort.save(clientToSave);

        return OAuth2CreatedClient.builder()
                .id(clientSaved.id())
                .clientId(clientSaved.clientId())
                .clientName(clientSaved.clientName())
                .clientSecret(clientSecret) // uniquement pour la création
                .applicationCode(clientSaved.applicationCode())
                .build();
    }


    protected String generateClientId() {
        for (int i = 0; i < MAX_CLIENT_ID_GENERATION_ATTEMPTS; i++) {
            final String clientId = this.oauth2ClientCredentialsGeneratorPort.generateClientId();

            if (this.oauth2ClientRepositoryPort.findByClientId(clientId).isEmpty()) {
                return clientId;
            }
        }

        throw new AuthServerFunctionalException(CLIENT_ID_GENERATION_FAILED, ERROR_CLIENT_ID_GENERATION_FAILED);
    }
}
