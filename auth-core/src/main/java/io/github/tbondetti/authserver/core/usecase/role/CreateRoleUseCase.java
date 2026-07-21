package io.github.tbondetti.authserver.core.usecase.role;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.core.usecase.application.GetApplicationUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.constants.RoleRules.CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.RoleRules.DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.RoleRules.NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeName;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class CreateRoleUseCase {
    static final String ERROR_CODE_MUST_BE_UNIQUE = "Le code doit être unique pour le code application %s.";

    private final RoleRepositoryPort roleRepositoryPort;
    private final GetApplicationUseCase getApplicationUseCase;

    public Role execute(
            final String applicationCode,
            final String code,
            final String name,
            final String description
    ) {
        final Application application = this.getApplicationUseCase.execute(applicationCode);

        final String normalizedCode = validateAndNormalizeCode(code, CODE_MAX_LENGTH);
        final String normalizedName = validateAndNormalizeName(name, NAME_MAX_LENGTH);
        final String normalizedDescription = normalizeAndValidateDescription(description, DESCRIPTION_MAX_LENGTH);

        this.ensureCodeIsUniqueForApplication(application.code(), normalizedCode);

        final Role role = Role.builder()
                .id(randomUUID())
                .codeApplication(application.code())
                .code(normalizedCode)
                .name(normalizedName)
                .description(normalizedDescription)
                .build();

        return this.roleRepositoryPort.save(role);
    }

    void ensureCodeIsUniqueForApplication(
            final String applicationCode,
            final String normalizedCode
    ) {
        final Optional<Role> optionalRole = this.roleRepositoryPort.findByApplicationCodeAndCode(
                applicationCode,
                normalizedCode
        );

        if (optionalRole.isPresent()) {
            throw new AuthServerFunctionalException(ERROR_CODE_MUST_BE_UNIQUE);
        }
    }
}
