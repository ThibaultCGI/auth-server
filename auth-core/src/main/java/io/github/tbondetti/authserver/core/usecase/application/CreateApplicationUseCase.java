package io.github.tbondetti.authserver.core.usecase.application;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.APPLICATION_CODE_ALREADY_EXISTS;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeName;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class CreateApplicationUseCase {
    static final String ERROR_CODE_MUST_BE_UNIQUE = "Le code doit être unique.";

    private final ApplicationRepositoryPort applicationRepositoryPort;

    public Application execute(
            final String code,
            final String name,
            final String description
    ) {
        final String normalizedCode = validateAndNormalizeCode(code, CODE_MAX_LENGTH);
        final String normalizedName = validateAndNormalizeName(name, NAME_MAX_LENGTH);
        final String normalizedDescription = normalizeAndValidateDescription(description, DESCRIPTION_MAX_LENGTH);

        this.ensureCodeIsUnique(normalizedCode);

        final Application application = Application.builder()
                .id(randomUUID())
                .code(normalizedCode)
                .name(normalizedName)
                .description(normalizedDescription)
                .build();

        return this.applicationRepositoryPort.save(application);
    }

    void ensureCodeIsUnique(final String normalizedCode) {
        final Optional<Application> optionalApplication = this.applicationRepositoryPort.findByCode(normalizedCode);

        if (optionalApplication.isPresent()) {
            throw new AuthServerFunctionalException(APPLICATION_CODE_ALREADY_EXISTS, ERROR_CODE_MUST_BE_UNIQUE);
        }
    }
}
