package io.github.tbondetti.authserver.core.usecase.role;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.ROLE_NOT_FOUND;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeCode;

@RequiredArgsConstructor
public class GetRoleUseCase {
    static final String ERROR_ROLE_NOT_FOUND = "Aucun rôle avec le code %s pour l'application %s n'est présent dans le référentiel.";

    private final RoleRepositoryPort roleRepositoryPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public Role execute(
            final String applicationCode,
            final String code
    ) {
        final String normalizedCode = normalizeCode(code);
        final Application application = this.getApplicationUseCase.execute(applicationCode);

        return this.roleRepositoryPort.findByApplicationCodeAndCode(application.code(), normalizedCode).orElseThrow(
                () -> new AuthServerFunctionalException(
                        ROLE_NOT_FOUND,
                        ERROR_ROLE_NOT_FOUND.formatted(normalizedCode, application.code())
                )
        );
    }
}
