package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper;
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

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {

    @InjectMocks
    private RoleRepositoryAdapter subject;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private ApplicationJpaRepository applicationJpaRepository;

    @Mock
    private RoleJpaRepository roleJpaRepository;

    @Mock
    private UserRoleJpaRepository userRoleJpaRepository;


    @Test
    void findByCodeAndApplicationCodeOk() {
        final String giveCode = "givenCode";
        final String givenApplicationCode = "givenApplicationCode";

        final ApplicationEntity foundApplication = new ApplicationEntity();
        when(this.applicationJpaRepository.getByCode(givenApplicationCode)).thenReturn(foundApplication);

        final RoleEntity foundRole = new RoleEntity();
        when(this.roleJpaRepository.findByCodeAndApplication(giveCode, foundApplication)).thenReturn(Optional.of(foundRole));

        final Role mappedRole = Role.builder().build();

        try (final MockedStatic<RoleMapper> utilities = mockStatic(RoleMapper.class)) {
            utilities.when(() -> toDomain(foundRole)).thenReturn(mappedRole); // déjà testé

            assertEquals(Optional.of(mappedRole), this.subject.findByCodeAndApplicationCode(giveCode, givenApplicationCode));
        }
    }

    @Test
    void findByCodeAndApplicationCodeAndUsernameOk() {
        final String giveCode = "givenCode";
        final String givenApplicationCode = "givenApplicationCode";
        final String givenUsername = "givenUsername";

        final ApplicationEntity foundApplication = new ApplicationEntity();
        when(this.applicationJpaRepository.getByCode(givenApplicationCode)).thenReturn(foundApplication);

        final UserEntity foundUser = new UserEntity();
        when(this.userJpaRepository.getByUsername(givenUsername)).thenReturn(foundUser);

        final RoleEntity foundRole = new RoleEntity();
        when(this.userRoleJpaRepository.findByCodeAndApplicationAndUser(giveCode, foundApplication, foundUser)).thenReturn(Optional.of(foundRole));

        final Role mappedRole = Role.builder().build();

        try (final MockedStatic<RoleMapper> utilities = mockStatic(RoleMapper.class)) {
            utilities.when(() -> toDomain(foundRole)).thenReturn(mappedRole); // déjà testé

            assertEquals(Optional.of(mappedRole), this.subject.findByCodeAndApplicationCodeAndUsername(giveCode, givenApplicationCode, givenUsername));
        }
    }


    @Test
    void saveOk() {
        final String applicationCode = "applicationCode";
        final Role given = Role.builder()
                .codeApplication(applicationCode)
                .build();

        final ApplicationEntity foundApplication = new ApplicationEntity();
        when(this.applicationJpaRepository.getByCode(applicationCode)).thenReturn(foundApplication);

        final RoleEntity mapped = new RoleEntity();
        final RoleEntity saved = new RoleEntity();

        final Role expected = Role.builder().build();

        try (final MockedStatic<RoleMapper> utilities = mockStatic(RoleMapper.class)) {
            utilities.when(() -> toEntity(given, foundApplication)).thenReturn(mapped); // déjà testé

            when(this.roleJpaRepository.save(mapped)).thenReturn(saved);

            utilities.when(() -> toDomain(saved)).thenReturn(expected); // déjà testé

            assertSame(expected, this.subject.save(given));
        }
    }
}