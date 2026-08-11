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
import io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_ID_GENERATION_FAILED;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.CLIENT_CREDENTIALS;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.REFRESH_TOKEN;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_ID_GENERATION_FAILED;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeGrantTypes;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeRedirectUris;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientName;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateGrantTypes;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateRedirectUris;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOAuth2ClientUseCaseTest {

    @Spy
    @InjectMocks
    private CreateOAuth2ClientUseCase subject;

    @Mock
    private OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    @Mock
    private OAuth2ClientCredentialsGeneratorPort oauth2ClientCredentialsGeneratorPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private GetApplicationUseCase getApplicationUseCase;


    @Test
    void generateClientIdKo() {
        final String clientId = "clientId";
        when(this.oauth2ClientCredentialsGeneratorPort.generateClientId()).thenReturn(clientId);

        final OAuth2Client client = OAuth2Client.builder().build();
        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.of(client));

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.generateClientId()
        );

        assertSame(CLIENT_ID_GENERATION_FAILED, exception.getCode());
        assertSame(ERROR_CLIENT_ID_GENERATION_FAILED, exception.getMessage());
    }

    @Test
    void generateClientIdOk() {
        final String clientId = "clientId";
        when(this.oauth2ClientCredentialsGeneratorPort.generateClientId()).thenReturn(clientId);

        when(this.oauth2ClientRepositoryPort.findByClientId(clientId)).thenReturn(Optional.empty());

        assertSame(clientId, this.subject.generateClientId());
    }

    @Test
    void executeOk() {
        final String clientName = "clientName";
        final String applicationCode = "applicationCode";
        final Collection<String> grantTypes = List.of("authorization_code", "refresh_token");
        final Collection<String> redirectUris = List.of("uri1", "uri2");

        try (final MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(
                OAuth2ClientValidationUtils.class,
                CALLS_REAL_METHODS
        )) {

            final String normalizedClientName = "normalizedClientName";
            utilities.when(() -> validateAndNormalizeClientName(clientName)).thenReturn(normalizedClientName); // déjà testé

            final String applicationApplicationCode = "applicationApplicationCode";
            final Application application = Application.builder()
                    .code(applicationApplicationCode)
                    .build();
            when(this.getApplicationUseCase.execute(applicationCode)).thenReturn(application);

            final Set<OAuth2ClientGrantType> normalizedGrantTypes = Set.of(
                    AUTHORIZATION_CODE,
                    REFRESH_TOKEN
            );
            utilities.when(() -> normalizeGrantTypes(grantTypes)).thenReturn(normalizedGrantTypes); // déjà testé
            utilities.when(() -> validateGrantTypes(normalizedGrantTypes)).thenAnswer(_ -> null); // déjà testé

            final Set<URI> normalizedRedirectUris = Set.of(
                    URI.create("https://localhost/callback"),
                    URI.create("https://localhost/callback2")
            );
            utilities.when(() -> normalizeRedirectUris(redirectUris)).thenReturn(normalizedRedirectUris); // déjà testé
            utilities.when(() -> validateRedirectUris(normalizedGrantTypes, normalizedRedirectUris)).thenAnswer(_ -> null); // déjà testé


            final String clientId = "clientId";
            doReturn(clientId).when(this.subject).generateClientId(); // déjà testé

            final String clientSecret = "clientSecret";
            when(this.oauth2ClientCredentialsGeneratorPort.generateClientSecret()).thenReturn(clientSecret);

            final String clientSecretHash = "clientSecretHash";
            when(this.passwordEncoderPort.encode(clientSecret)).thenReturn(clientSecretHash);

            final UUID id = randomUUID();
            try (final MockedStatic<UUID> uuidUtilities = mockStatic(UUID.class)) {
                uuidUtilities.when(UUID::randomUUID).thenReturn(id);

                final OAuth2Client clientToSave = OAuth2Client.builder()
                        .id(id)
                        .clientId(clientId)
                        .clientName(normalizedClientName)
                        .clientSecretHash(clientSecretHash)
                        .applicationCode(applicationApplicationCode)
                        .grantTypes(normalizedGrantTypes)
                        .redirectUris(normalizedRedirectUris)
                        .build();

                final String clientIdSaved = "clientIdSaved";
                final String clientNameSaved = "clientNameSaved";
                final String clientSecretHashSaved = "clientSecretHashSaved";
                final String applicationCodeSaved = "applicationCodeSaved";
                final Set<OAuth2ClientGrantType> grantTypesSaved = Set.of(CLIENT_CREDENTIALS);
                final Set<URI> redirectUrisSaved = Set.of(
                        URI.create("test")
                );

                final OAuth2Client clientSaved = OAuth2Client.builder()
                        .id(id)
                        .clientId(clientIdSaved)
                        .clientName(clientNameSaved)
                        .clientSecretHash(clientSecretHashSaved)
                        .applicationCode(applicationCodeSaved)
                        .grantTypes(grantTypesSaved)
                        .redirectUris(redirectUrisSaved)
                        .build();

                when(this.oauth2ClientRepositoryPort.save(clientToSave)).thenReturn(clientSaved);

                final OAuth2CreatedClient expected = OAuth2CreatedClient.builder()
                        .id(id)
                        .clientId(clientIdSaved)
                        .clientName(clientNameSaved)
                        .clientSecret(clientSecret)
                        .applicationCode(applicationCodeSaved)
                        .grantTypes(grantTypesSaved)
                        .redirectUris(redirectUrisSaved)
                        .build();

                assertEquals(expected, this.subject.execute(
                        clientName,
                        applicationCode,
                        grantTypes,
                        redirectUris
                ));
            }
        }
    }
}
