package io.github.tbondetti.authserver.persistence.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.persistence.entity.OAuth2ScopeEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.persistence.mapper.OAuth2ScopeMapper.toDomain;
import static io.github.tbondetti.authserver.persistence.mapper.OAuth2ScopeMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class OAuth2ScopeMapperTest {

    @Test
    void toDomainOk() {
        final UUID id = UUID.randomUUID();

        final String applicationCode = "applicationCode";
        final ApplicationEntity application = new ApplicationEntity();
        application.setCode(applicationCode);

        final String code = "code";
        final String name = "name";
        final String description = "description";

        final OAuth2ScopeEntity entity = new OAuth2ScopeEntity(
                id,
                code,
                name,
                description,
                application
        );

        final OAuth2Scope expected = OAuth2Scope.builder()
                .id(id)
                .applicationCode(applicationCode)
                .code(code)
                .name(name)
                .description(description)
                .build();

        assertEquals(expected, toDomain(entity));
    }

    @Test
    void toEntityOk() {
        final UUID id = UUID.randomUUID();

        final String applicationCode = "applicationCode";
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final OAuth2Scope domain = OAuth2Scope.builder()
                .id(id)
                .applicationCode(applicationCode)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final ApplicationEntity application = new ApplicationEntity();
        application.setCode(applicationCode);

        final OAuth2ScopeEntity entity = toEntity(domain, application);

        assertSame(id, entity.getId());
        assertEquals(code, entity.getCode());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertSame(application, entity.getApplication());
    }
}
