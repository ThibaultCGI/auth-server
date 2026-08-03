package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.core.port.ApplicationRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper.toEntity;

@RequiredArgsConstructor
public class ApplicationRepositoryAdapter implements ApplicationRepositoryPort {

    private final ApplicationJpaRepository applicationJpaRepository;

    @Override
    public Optional<Application> findByCode(final String code) {
        return this.applicationJpaRepository.findByCode(code).map(ApplicationMapper::toDomain);
    }

    @Override
    public Application save(final Application application) {
        final ApplicationEntity newApplication = toEntity(application);
        return toDomain(this.applicationJpaRepository.save(newApplication));
    }
}
