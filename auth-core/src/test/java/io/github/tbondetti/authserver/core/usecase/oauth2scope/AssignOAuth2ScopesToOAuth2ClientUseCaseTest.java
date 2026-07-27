package io.github.tbondetti.authserver.core.usecase.oauth2scope;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientScopeRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.oauth2client.GetOAuth2ClientUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_ALREADY_ASSIGNED;
import static io.github.tbondetti.authserver.core.usecase.oauth2scope.AssignOAuth2ScopesToOAuth2ClientUseCase.ERROR_SCOPE_ALREADY_ASSIGNED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignOAuth2ScopesToOAuth2ClientUseCaseTest {

    @InjectMocks
    private AssignOAuth2ScopesToOAuth2ClientUseCase subject;

    @Mock
    private OAuth2ClientScopeRepositoryPort oauth2ClientScopeRepositoryPort;

    @Mock
    private GetOAuth2ScopeUseCase getOAuth2ScopeUseCase;

    @Mock
    private GetOAuth2ClientUseCase getOAuth2ClientUseCase;


    @Test
    void executeKoWhenScopeAlreadyAssigned() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String clientId = "clientId";

        final String normalizedApplicationCode = "normalizedApplicationCode";
        final String normalizedCode = "normalizedCode";
        final OAuth2Scope scope = OAuth2Scope.builder()
                .applicationCode(normalizedApplicationCode)
                .code(normalizedCode)
                .build();
        when(this.getOAuth2ScopeUseCase.execute(applicationCode, code)).thenReturn(scope);

        final String normalizedClientId = "normalizedClientId";
        final OAuth2Client client = OAuth2Client.builder()
                .clientId(normalizedClientId)
                .build();
        when(this.getOAuth2ClientUseCase.execute(clientId)).thenReturn(client);

        when(this.oauth2ClientScopeRepositoryPort.exists(
                normalizedApplicationCode,
                normalizedCode,
                normalizedClientId
        )).thenReturn(true);

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> this.subject.execute(applicationCode, code, clientId)
        );

        assertSame(SCOPE_ALREADY_ASSIGNED, exception.getCode());

        assertEquals(
                ERROR_SCOPE_ALREADY_ASSIGNED.formatted(scope.completeCode(), normalizedClientId),
                exception.getMessage()
        );
    }

    @Test
    void executeOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String clientId = "clientId";

        final String normalizedApplicationCode = "normalizedApplicationCode";
        final String normalizedCode = "normalizedCode";
        final OAuth2Scope scope = OAuth2Scope.builder()
                .applicationCode(normalizedApplicationCode)
                .code(normalizedCode)
                .build();
        when(this.getOAuth2ScopeUseCase.execute(applicationCode, code)).thenReturn(scope);

        final String normalizedClientId = "normalizedClientId";
        final OAuth2Client client = OAuth2Client.builder()
                .clientId(normalizedClientId)
                .build();
        when(this.getOAuth2ClientUseCase.execute(clientId)).thenReturn(client);

        when(this.oauth2ClientScopeRepositoryPort.exists(
                normalizedApplicationCode,
                normalizedCode,
                normalizedClientId
        )).thenReturn(false);

        this.subject.execute(applicationCode, code, clientId);

        verify(this.oauth2ClientScopeRepositoryPort, times(1)).assign(
                normalizedApplicationCode,
                normalizedCode,
                normalizedClientId
        );
    }
}