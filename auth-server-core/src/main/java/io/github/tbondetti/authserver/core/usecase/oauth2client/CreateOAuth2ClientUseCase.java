package io.github.tbondetti.authserver.core.usecase.oauth2client;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientCredentialsGeneratorPort;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.port.PasswordEncoderPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_ID_GENERATION_FAILED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_ID_GENERATION_FAILED;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeGrantTypes;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeRedirectUris;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientName;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateGrantTypes;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateRedirectUris;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class CreateOAuth2ClientUseCase {

    static final int MAX_CLIENT_ID_GENERATION_ATTEMPTS = 5;

    private final OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;
    private final OAuth2ClientCredentialsGeneratorPort oauth2ClientCredentialsGeneratorPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public OAuth2CreatedClient execute(
            final String clientName,
            final String applicationCode,
            final Collection<String> grantTypes,
            final Collection<String> redirectUris
    ) {
        final String normalizedClientName = validateAndNormalizeClientName(clientName);

        final Application application = this.getApplicationUseCase.execute(applicationCode);

        final Set<OAuth2ClientGrantType> normalizedGrantTypes = normalizeGrantTypes(grantTypes);
        validateGrantTypes(normalizedGrantTypes);

        final Set<URI> normalizedRedirectUris = normalizeRedirectUris(redirectUris);
        validateRedirectUris(normalizedGrantTypes, normalizedRedirectUris);

        final String clientId = this.generateClientId();
        final String clientSecret = this.oauth2ClientCredentialsGeneratorPort.generateClientSecret();

        final String clientSecretHash = this.passwordEncoderPort.encode(clientSecret);

        final OAuth2Client clientToSave = OAuth2Client.builder()
                .id(randomUUID())
                .clientId(clientId)
                .clientName(normalizedClientName)
                .clientSecretHash(clientSecretHash)
                .applicationCode(application.code())
                .grantTypes(normalizedGrantTypes)
                .redirectUris(normalizedRedirectUris)
                .build();

        final OAuth2Client clientSaved = this.oauth2ClientRepositoryPort.save(clientToSave);

        return OAuth2CreatedClient.builder()
                .id(clientSaved.id())
                .clientId(clientSaved.clientId())
                .clientName(clientSaved.clientName())
                .clientSecret(clientSecret) // uniquement pour la création
                .applicationCode(clientSaved.applicationCode())
                .grantTypes(clientSaved.grantTypes())
                .redirectUris(clientSaved.redirectUris())
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
