package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.RoleJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toEntity;


@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final ApplicationJpaRepository applicationJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public Optional<Role> findByApplicationCodeAndCode(
            final String applicationCode,
            final String code
    ) {
        return this.roleJpaRepository.findByApplicationCodeAndCode(applicationCode, code).map(RoleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByCodeAndApplicationCodeAndUsername(
            final String code,
            final String applicationCode,
            final String username
    ) {
        return this.userRoleJpaRepository.findByCodeAndApplicationCodeAndUsername(
                    code,
                    applicationCode,
                    username
                )
                .map(RoleMapper::toDomain);
    }

    @Override
    public Role save(final Role role) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(role.codeApplication());
        final RoleEntity newRole = toEntity(role, application);
        final RoleEntity savedRole = this.roleJpaRepository.save(newRole);

        return toDomain(this.roleJpaRepository.getByApplicationCodeAndCode(
                savedRole.getApplication().getCode(),
                savedRole.getCode()
        ));
    }

    @Override
    public void delete(final Role role) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(role.codeApplication());
        final RoleEntity roleToDelete = this.roleJpaRepository.getByApplicationCodeAndCode(application.getCode(), role.code());

        this.roleJpaRepository.delete(roleToDelete);
    }
}
