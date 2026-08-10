package io.github.tbondetti.authserver.core.usecase.oauth2scope;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.exception.AuthServerNotFoundException;
import io.github.tbondetti.authserver.core.port.OAuth2ScopeRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_NOT_FOUND;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.normalizeCode;

@RequiredArgsConstructor
public class GetOAuth2ScopeUseCase {

    private final OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public OAuth2Scope execute(
            final String applicationCode,
            final String code
    ) {
        final Application application = this.getApplicationUseCase.execute(applicationCode);
        final String normalizedCode = normalizeCode(code);

        return this.oauth2ScopeRepositoryPort.findByApplicationCodeAndCode(
                application.code(),
                normalizedCode
        ).orElseThrow(() -> new AuthServerNotFoundException(
                SCOPE_NOT_FOUND,
                ERROR_SCOPE_NOT_FOUND.formatted(normalizedCode, application.code())
        ));
    }
}
