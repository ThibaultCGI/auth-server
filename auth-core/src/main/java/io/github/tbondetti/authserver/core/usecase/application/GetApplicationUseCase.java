package io.github.tbondetti.authserver.core.usecase.application;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import lombok.RequiredArgsConstructor;

import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.normalizeCode;

@RequiredArgsConstructor
public class GetApplicationUseCase {
    static final String ERROR_APPLICATION_NOT_FOUND = "Aucune application avec le code %s n'est présente dans le référentiel.";

    private final ApplicationRepositoryPort applicationRepositoryPort;

    public Application execute(final String code) {
        final String normalizedCode = normalizeCode(code);

        return this.applicationRepositoryPort.findByCode(normalizedCode).orElseThrow(
                () -> new AuthServerFunctionalException(ERROR_APPLICATION_NOT_FOUND.formatted(normalizedCode))
        );
    }
}
