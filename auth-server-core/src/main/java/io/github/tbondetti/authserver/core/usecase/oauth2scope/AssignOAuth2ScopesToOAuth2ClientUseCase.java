package io.github.tbondetti.authserver.core.usecase.oauth2scope;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ClientScopeRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.oauth2client.GetOAuth2ClientUseCase;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_ALREADY_ASSIGNED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_ALREADY_ASSIGNED;

@RequiredArgsConstructor
public class AssignOAuth2ScopesToOAuth2ClientUseCase {

    private final OAuth2ClientScopeRepositoryPort oauth2ClientScopeRepositoryPort;
    private final GetOAuth2ScopeUseCase getOAuth2ScopeUseCase;
    private final GetOAuth2ClientUseCase getOAuth2ClientUseCase;

    public void execute(
            final String applicationCode,
            final String code,
            final String clientId
    ) {
        final OAuth2Scope scope = this.getOAuth2ScopeUseCase.execute(applicationCode, code);
        final OAuth2Client client = this.getOAuth2ClientUseCase.execute(clientId);

        if (this.oauth2ClientScopeRepositoryPort.exists(scope.applicationCode(), scope.code(), client.clientId())) {
            throw new AuthServerFunctionalException(
                    SCOPE_ALREADY_ASSIGNED,
                    ERROR_SCOPE_ALREADY_ASSIGNED.formatted(scope.completeCode(), client.clientId())
            );
        }

        this.oauth2ClientScopeRepositoryPort.assign(scope.applicationCode(), scope.code(), client.clientId());
    }
}
