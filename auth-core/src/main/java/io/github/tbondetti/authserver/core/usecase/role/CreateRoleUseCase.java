package io.github.tbondetti.authserver.core.usecase.role;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.utils.RoleValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.RoleValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.RoleValidationUtils.validateAndNormalizeName;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class CreateRoleUseCase {
    static final String ERROR_CODE_MUST_BE_UNIQUE = "Le code doit être unique pour le code application %s.";

    private final RoleRepositoryPort roleRepositoryPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public Role execute(
            final String code,
            final String name,
            final String description,
            final String applicationCode
    ) {
        final String normalizedCode = validateAndNormalizeCode(code);
        final String normalizedName = validateAndNormalizeName(name);
        final String normalizedDescription = normalizeAndValidateDescription(description);

        final Application application = this.getApplicationUseCase.execute(applicationCode);

        this.ensureCodeIsUniqueForApplication(normalizedCode, application.code());

        final Role role = Role.builder()
                .id(randomUUID())
                .code(normalizedCode)
                .name(normalizedName)
                .description(normalizedDescription)
                .codeApplication(application.code())
                .build();

        return this.roleRepositoryPort.save(role);
    }

    void ensureCodeIsUniqueForApplication(final String normalizedCode,
                                          final String applicationCode
    ) {
        final Optional<Role> optionalRole = this.roleRepositoryPort.findByCodeAndApplicationCode(
                normalizedCode,
                applicationCode
        );

        if (optionalRole.isPresent()) {
            throw new AuthServerFunctionalException(ERROR_CODE_MUST_BE_UNIQUE);
        }
    }
}
