package io.github.tbondetti.authserver.core.usecase.oauth2scope;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.OAuth2ScopeRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.validateAndNormalizeName;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class CreateOAuth2ScopeUseCase {

    static final String ERROR_SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION = "Le code du scope doit être unique pour l'application.";

    private final OAuth2ScopeRepositoryPort oauth2ScopeRepositoryPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public OAuth2Scope execute(
            final String applicationCode,
            final String code,
            final String name,
            final String description
    ) {
        final Application application = this.getApplicationUseCase.execute(applicationCode);
        final String normalizedCode = validateAndNormalizeCode(code);
        final String normalizedName = validateAndNormalizeName(name);
        final String normalizedDescription = normalizeAndValidateDescription(description);

        this.ensureCodeIsUniqueForApplication(application.code(), normalizedCode);

        final OAuth2Scope scope = OAuth2Scope.builder()
                .id(randomUUID())
                .applicationCode(application.code())
                .code(normalizedCode)
                .name(normalizedName)
                .description(normalizedDescription)
                .build();

        return this.oauth2ScopeRepositoryPort.save(scope);
    }


    void ensureCodeIsUniqueForApplication(
            final String applicationCode,
            final String code
    ) {
        this.oauth2ScopeRepositoryPort.findByApplicationCodeAndCode(
                applicationCode,
                code
        ).ifPresent(existingScope -> {
            throw new AuthServerFunctionalException(
                    SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION, ERROR_SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION
            );
        });
    }
}
