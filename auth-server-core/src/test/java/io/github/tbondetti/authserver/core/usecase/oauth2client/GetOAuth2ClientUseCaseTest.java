package io.github.tbondetti.authserver.core.usecase.oauth2client;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NOT_FOUND;
import static io.github.tbondetti.authserver.core.usecase.oauth2client.GetOAuth2ClientUseCase.ERROR_CLIENT_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeClientId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GetOAuth2ClientUseCaseTest {
    @InjectMocks
    private GetOAuth2ClientUseCase subject;

    @Mock
    private OAuth2ClientRepositoryPort oauth2ClientRepositoryPort;

    @Test
    void executeKo() {
        final String clientId = "clientId";
        final String normalizedClientId = "normalizedClientId";

        try(final MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(OAuth2ClientValidationUtils.class)) {
            utilities.when(() -> normalizeClientId(clientId)).thenReturn(normalizedClientId);

            when(this.oauth2ClientRepositoryPort.findByClientId(normalizedClientId)).thenReturn(Optional.empty());

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> this.subject.execute(clientId)
            );

            assertSame(CLIENT_NOT_FOUND, exception.getCode());
            assertEquals(ERROR_CLIENT_NOT_FOUND.formatted(normalizedClientId), exception.getMessage());
        }
    }

    @Test
    void executeOk() {
        final String clientId = "clientId";
        final String normalizedClientId = "normalizedClientId";

        try(final MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(OAuth2ClientValidationUtils.class)) {
            utilities.when(() -> normalizeClientId(clientId)).thenReturn(normalizedClientId);

            final OAuth2Client client = OAuth2Client.builder().build();
            when(this.oauth2ClientRepositoryPort.findByClientId(normalizedClientId)).thenReturn(Optional.of(client));

            assertSame(client, this.subject.execute(clientId));
        }
    }
}
