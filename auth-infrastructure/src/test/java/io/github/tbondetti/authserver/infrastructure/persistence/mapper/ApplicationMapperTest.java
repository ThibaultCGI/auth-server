package io.github.tbondetti.authserver.infrastructure.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.ApplicationMapper.toEntity;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ApplicationMapperTest {

    @Test
    void toDomainOk() {
        final UUID id = randomUUID();
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final ApplicationEntity applicationEntity = new ApplicationEntity(
                id,
                code,
                name,
                description
        );

        final Application expected = Application.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .build();

        assertEquals(expected, toDomain(applicationEntity));
    }

    @Test
    void toEntityOk() {
        final UUID id = randomUUID();
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final Application application = Application.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final ApplicationEntity actual = toEntity(application);

        assertSame(id, actual.getId());
        assertSame(code, actual.getCode());
        assertSame(name, actual.getName());
        assertSame(description, actual.getDescription());
    }

}