package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.port.RoleRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.RoleJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toEntity;


@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final ApplicationJpaRepository applicationJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public Optional<Role> findByCodeAndApplicationCode(
            final String code,
            final String applicationCode
    ) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(applicationCode);

        return this.roleJpaRepository.findByCodeAndApplication(code, application).map(RoleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByCodeAndApplicationCodeAndUsername(
            final String code,
            final String applicationCode,
            final String username
    ) {
        final ApplicationEntity applicationEntity = this.applicationJpaRepository.getByCode(applicationCode);
        final UserEntity userEntity = this.userJpaRepository.getByUsername(username);

        return this.userRoleJpaRepository.findByCodeAndApplicationAndUser(code, applicationEntity, userEntity)
                .map(RoleMapper::toDomain);
    }

    @Override
    public Role save(final Role role) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(role.codeApplication());
        final RoleEntity newRole = toEntity(role, application);
        final RoleEntity savedRole = this.roleJpaRepository.save(newRole);

        final RoleEntity savedRoleWithApplication = this.roleJpaRepository.getByApplicationAndCode(
                savedRole.getApplication(),
                savedRole.getCode()
        );

        return toDomain(savedRoleWithApplication);
    }
}
