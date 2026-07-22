package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.Application;
import io.github.tbondetti.authserver.infrastructure.web.response.ApplicationResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.ApplicationWebMapper.toResponse;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationWebMapperTest {

    @Test
    void toResponseOk() {
        final UUID id = randomUUID();
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final Application role = Application.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final ApplicationResponse expected = ApplicationResponse.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final ApplicationResponse actual = toResponse(role);
        assertEquals(expected, actual);
    }
}