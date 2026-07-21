package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.RoleJpaRepository;
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
    private ApplicationJpaRepository applicationJpaRepository;

    @Mock
    private RoleJpaRepository roleJpaRepository;

    @Mock
    private UserRoleJpaRepository userRoleJpaRepository;


    @Test
    void findByApplicationCodeAndCodeOk() {
        final String givenApplicationCode = "givenApplicationCode";
        final String giveCode = "givenCode";

        final RoleEntity foundRole = new RoleEntity();
        when(this.roleJpaRepository.findByApplicationCodeAndCode(givenApplicationCode, giveCode)).thenReturn(Optional.of(foundRole));

        final Role mappedRole = Role.builder().build();

        try (final MockedStatic<RoleMapper> utilities = mockStatic(RoleMapper.class)) {
            utilities.when(() -> toDomain(foundRole)).thenReturn(mappedRole); // déjà testé

            assertEquals(Optional.of(mappedRole), this.subject.findByApplicationCodeAndCode(givenApplicationCode, giveCode));
        }
    }

    @Test
    void findByCodeAndApplicationCodeAndUsernameOk() {
        final String giveCode = "givenCode";
        final String givenApplicationCode = "givenApplicationCode";
        final String givenUsername = "givenUsername";

        final RoleEntity foundRole = new RoleEntity();
        when(this.userRoleJpaRepository.findByCodeAndApplicationCodeAndUsername(giveCode, givenApplicationCode, givenUsername)).thenReturn(Optional.of(foundRole));

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

        final ApplicationEntity application = new ApplicationEntity();
        when(this.applicationJpaRepository.getByCode(applicationCode)).thenReturn(application);

        final String applicationCodeSaved = "applicationCodeSaved";
        final ApplicationEntity applicationSaved = new ApplicationEntity();
        applicationSaved.setCode(applicationCodeSaved);

        final String roleCodeSaved = "roleCodeSaved";
        final RoleEntity savedRole = new RoleEntity();
        savedRole.setCode(roleCodeSaved);
        savedRole.setApplication(applicationSaved);

        final RoleEntity roleFound = new RoleEntity();

        final Role expected = Role.builder().build();

        try (final MockedStatic<RoleMapper> utilities = mockStatic(RoleMapper.class)) {
            final RoleEntity newRole = new RoleEntity();
            utilities.when(() -> toEntity(given, application)).thenReturn(newRole); // déjà testé

            when(this.roleJpaRepository.save(newRole)).thenReturn(savedRole);

            when(this.roleJpaRepository.getByApplicationCodeAndCode(
                    applicationCodeSaved,
                    roleCodeSaved
            )).thenReturn(roleFound);

            utilities.when(() -> toDomain(roleFound)).thenReturn(expected); // déjà testé

            assertSame(expected, this.subject.save(given));
        }
    }
}