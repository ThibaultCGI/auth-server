package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.Role;
import io.github.tbondetti.authserver.infrastructure.web.response.RoleResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.RoleWebMapper.toResponse;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleWebMapperTest {


    @Test
    void toResponseOk() {
        final String codeApplication = "codeApplication";
        final UUID id = randomUUID();
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final Role role = Role.builder()
                .codeApplication(codeApplication)
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final RoleResponse expected = RoleResponse.builder()
                .codeApplication(codeApplication)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final RoleResponse actual = toResponse(role);
        assertEquals(expected, actual);
    }
}