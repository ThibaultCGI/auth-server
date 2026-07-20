package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.core.domain.User;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserRoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserRoleId;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.RoleJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.UserRoleJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.constants.TestConstants.USER_NAME;
import static io.github.tbondetti.authserver.infrastructure.persistence.adapter.UserRepositoryAdapter.newUserRoleEntity;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.UserMapper.toEntity;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @InjectMocks
    private UserRepositoryAdapter subject;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private ApplicationJpaRepository applicationJpaRepository;

    @Mock
    private RoleJpaRepository roleJpaRepository;

    @Mock
    private UserRoleJpaRepository userRoleJpaRepository;

    @Test
    void findByUsernameOk() {
        final UserEntity userEntity = new UserEntity();
        final User expected = User.builder().build();

        when(this.userJpaRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(userEntity));
        try (final MockedStatic<UserMapper> utilities = mockStatic(UserMapper.class)) {
            utilities.when(() -> toDomain(userEntity)).thenReturn(expected); // déjà testé

            assertEquals(Optional.of(expected), this.subject.findByUsername(USER_NAME));
        }
    }

    @Test
    void saveOk() {
        final User givenUser = User.builder().build();

        final UserEntity mappedEntity = new UserEntity();
        final UserEntity savedEntity = new UserEntity();

        final User expected = User.builder().build();

        try (final MockedStatic<UserMapper> utilities = mockStatic(UserMapper.class)) {
            utilities.when(() -> toEntity(givenUser)).thenReturn(mappedEntity); // déjà testé

            when(this.userJpaRepository.save(mappedEntity)).thenReturn(savedEntity);

            utilities.when(() -> toDomain(savedEntity)).thenReturn(expected); // déjà testé

            assertSame(expected, this.subject.save(givenUser));
        }
    }

    @Test
    void getUserRolesForApplicationOk() {
        final String givenUsername = "givenUsername";
        final String givenApplicationCode = "givenApplicationCode";

        final UserEntity foundUser = new UserEntity();
        final ApplicationEntity foundApplication = new ApplicationEntity();

        final RoleEntity foundRole1 = new RoleEntity();
        final RoleEntity foundRole2 = new RoleEntity();

        final Role mappedRole1 = Role.builder().build();
        final Role mappedRole2 = Role.builder().build();

        when(this.userJpaRepository.getByUsername(givenUsername)).thenReturn(foundUser);
        when(this.applicationJpaRepository.getByCode(givenApplicationCode)).thenReturn(foundApplication);
        when(this.userRoleJpaRepository.findAllByUserAndApplication(foundUser, foundApplication)).thenReturn(List.of(
                foundRole1,
                foundRole2
        ));

        try (final MockedStatic<RoleMapper> utilities = mockStatic(RoleMapper.class)) {
            utilities.when(() -> RoleMapper.toDomain(foundRole1)).thenReturn(mappedRole1); // déjà testé
            utilities.when(() -> RoleMapper.toDomain(foundRole2)).thenReturn(mappedRole2); // déjà testé

            assertEquals(List.of(
                    mappedRole1,
                    mappedRole2
            ), this.subject.getUserRolesForApplication(givenUsername, givenApplicationCode));
        }
    }

    @Test
    void newUserRoleEntityOk() {
        final UUID userUuid = randomUUID();
        final UUID roleUuid = randomUUID();

        final UserRoleId userRoleId = new UserRoleId(userUuid, roleUuid);
        final UserEntity userEntity = new UserEntity();
        final RoleEntity roleEntity = new RoleEntity();

        final UserRoleEntity actual = newUserRoleEntity(userRoleId, userEntity, roleEntity);

        assertSame(userRoleId, actual.getId());
        assertSame(userEntity, actual.getUser());
        assertSame(roleEntity, actual.getRole());
    }

    @Test
    void addRoleToUserOk() {
        final String givenUsername = "givenUsername";
        final String givenApplicationCode = "givenApplicationCode";
        final String givenRoleCode = "givenRoleCode";

        final UUID userUuid = randomUUID();
        final UserEntity foundUser = new UserEntity();
        foundUser.setId(userUuid);

        final ApplicationEntity foundApplication = new ApplicationEntity();

        final UUID roleUuid = randomUUID();
        final RoleEntity foundRole = new RoleEntity();
        foundRole.setId(roleUuid);

        final UserRoleId userRoleId = new UserRoleId(
                userUuid,
                roleUuid
        );

        final UserRoleEntity userRoleToSave = new UserRoleEntity(
                userRoleId,
                foundUser,
                foundRole
        );

        when(this.userJpaRepository.getByUsername(givenUsername)).thenReturn(foundUser);
        when(this.applicationJpaRepository.getByCode(givenApplicationCode)).thenReturn(foundApplication);
        when(this.roleJpaRepository.getByApplicationAndCode(foundApplication, givenRoleCode)).thenReturn(foundRole);

        try (final MockedStatic<UserRepositoryAdapter> utilities = mockStatic(UserRepositoryAdapter.class)) {
            utilities.when(() -> newUserRoleEntity(userRoleId, foundUser, foundRole)).thenReturn(userRoleToSave); // déjà testé

            this.subject.addRoleToUser(givenUsername, givenApplicationCode, givenRoleCode);

            verify(this.userRoleJpaRepository, times(1)).save(userRoleToSave);
        }
    }
}