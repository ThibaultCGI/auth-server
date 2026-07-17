package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toEntity;


@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final ApplicationJpaRepository applicationJpaRepository;

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<Role> findByCodeAndApplicationCode(
            final String code,
            final String applicationCode
    ) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(applicationCode);

        return this.roleJpaRepository.findByCodeAndApplication(code, application).map(RoleMapper::toDomain);
    }

    @Override
    public Role save(final Role role) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(role.codeApplication());
        final RoleEntity newRole = toEntity(role, application);
        return toDomain(this.roleJpaRepository.save(newRole));
    }
}
