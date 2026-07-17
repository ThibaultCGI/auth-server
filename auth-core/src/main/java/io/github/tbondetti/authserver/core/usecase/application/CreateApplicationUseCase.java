package io.github.tbondetti.authserver.core.usecase.application;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.validateAndNormalizeName;
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
        final String normalizedCode = validateAndNormalizeCode(code);
        final String normalizedName = validateAndNormalizeName(name);
        final String normalizedDescription = normalizeAndValidateDescription(description);

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
            throw new AuthServerFunctionalException(ERROR_CODE_MUST_BE_UNIQUE);
        }
    }
}
