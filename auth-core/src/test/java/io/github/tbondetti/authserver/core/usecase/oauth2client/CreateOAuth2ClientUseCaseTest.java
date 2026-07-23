package io.github.tbondetti.authserver.core.usecase.oauth2client;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
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

import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_ID_GENERATION_FAILED;
import static io.github.tbondetti.authserver.core.usecase.oauth2client.CreateOAuth2ClientUseCase.ERROR_CLIENT_ID_GENERATION_FAILED;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientName;
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
    void executeOk() {
        final String clientName = "clientName";
        final String applicationCode = "applicationCode";

        try (MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(OAuth2ClientValidationUtils.class, CALLS_REAL_METHODS)) {
            final String normalizedClientName = "normalizedClientName";
            utilities.when(() -> validateAndNormalizeClientName(clientName)).thenReturn(normalizedClientName); // déjà testé

            final String applicationApplicationCode = "applicationApplicationCode";
            final Application application = Application.builder()
                    .code(applicationApplicationCode)
                    .build();
            when(this.getApplicationUseCase.execute(applicationCode)).thenReturn(application);

            final String clientId = "clientId";
            doReturn(clientId).when(this.subject).generateClientId(); // déjà testé

            final String clientSecret = "clientSecret";
            when(this.oauth2ClientCredentialsGeneratorPort.generateClientSecret()).thenReturn(clientSecret);

            final String clientSecretHash = "clientSecretHash";
            when(this.passwordEncoderPort.encode(clientSecret)).thenReturn(clientSecretHash);

            final OAuth2Client clientToSave = OAuth2Client.builder()
                    .clientId(clientId)
                    .clientName(normalizedClientName)
                    .clientSecretHash(clientSecretHash)
                    .applicationCode(applicationApplicationCode)
                    .build();

            final UUID idSaved = randomUUID();
            final String clientIdSaved = "clientIdSaved";
            final String clientNameSaved = "clientNameSaved";
            final String clientSecretHashSaved = "clientSecretHashSaved";
            final String applicationCodeSaved = "applicationCodeSaved";

            final OAuth2Client clientSaved =  OAuth2Client.builder()
                    .id(idSaved)
                    .clientId(clientIdSaved)
                    .clientName(clientNameSaved)
                    .clientSecretHash(clientSecretHashSaved)
                    .applicationCode(applicationCodeSaved)
                    .build();
            when(this.oauth2ClientRepositoryPort.save(clientToSave)).thenReturn(clientSaved);

            final OAuth2CreatedClient expected =  OAuth2CreatedClient.builder()
                    .id(idSaved)
                    .clientId(clientIdSaved)
                    .clientName(clientNameSaved)
                    .clientSecret(clientSecret) // uniquement pour la création
                    .applicationCode(applicationCodeSaved)
                    .build();

            assertEquals(expected, this.subject.execute(clientName, applicationCode));
        }
    }
}
