package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.core.port.UserRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserRoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserRoleId;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.RoleJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toEntity;

@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public Optional<User> findByUsername(final String username) {
        return this.userJpaRepository.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public User save(final User user) {
        final UserEntity newUser = toEntity(user);
        return toDomain(this.userJpaRepository.save(newUser));
    }

    @Override
    public List<Role> getUserRolesForApplication(final String username,
                                                 final String applicationCode
    ) {
        return this.userRoleJpaRepository.findAllByUsernameAndApplicationCode(
                username,
                applicationCode
        ).stream().map(RoleMapper::toDomain).toList();
    }

    @Override
    public List<Role> getAllUserRoles(final String username) {
        return this.userRoleJpaRepository.findAllByUsername(username).stream().map(RoleMapper::toDomain).toList();
    }

    @Override
    public void addRoleToUser(final String username,
                              final String applicationCode,
                              final String roleCode
    ) {
        final UserEntity userEntity = this.userJpaRepository.getByUsername(username);
        final RoleEntity roleEntity = this.roleJpaRepository.getByApplicationCodeAndCode(applicationCode, roleCode);

        final UserRoleId userRoleId = new UserRoleId(
                userEntity.getId(),
                roleEntity.getId()
        );

        this.userRoleJpaRepository.save(newUserRoleEntity(
                userRoleId,
                userEntity,
                roleEntity
        ));
    }

    static UserRoleEntity newUserRoleEntity(
            final UserRoleId userRoleId,
            final UserEntity userEntity,
            final RoleEntity roleEntity
    ) {
        return new UserRoleEntity(
                userRoleId,
                userEntity,
                roleEntity
        );
    }
}
