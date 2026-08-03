package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.RoleMapper.toEntity;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class RoleMapperTest {

    @Test
    void toDomainOk() {
        final UUID id = randomUUID();
        final String code = "code";
        final String name = "name";
        final String description = "description";
        final String codeApplication = "codeApplication";

        final ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setCode(codeApplication);

        final RoleEntity roleEntity = new RoleEntity(
                id,
                code,
                name,
                description,
                applicationEntity
        );

        final Role expected = Role.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .codeApplication(codeApplication)
                .build();

        assertEquals(expected, toDomain(roleEntity));
    }

    @Test
    void toEntityOk() {
        final UUID id = randomUUID();
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final Role role = Role.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final ApplicationEntity applicationEntity = new ApplicationEntity();

        final RoleEntity actual = toEntity(role, applicationEntity);

        assertSame(id, actual.getId());
        assertSame(code, actual.getCode());
        assertSame(name, actual.getName());
        assertSame(description, actual.getDescription());
        assertSame(applicationEntity, actual.getApplication());
    }

}