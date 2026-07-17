package io.github.tbondetti.authserver.core.port;

import io.github.tbondetti.authserver.core.domain.Application;

import java.util.Optional;

public interface ApplicationRepositoryPort {

    Optional<Application> findByCode(final String code);

    Application save(final Application application);
}
