package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.infrastructure.web.response.OAuth2ScopeResponse;
import org.junit.jupiter.api.Test;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ScopeWebMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OAuth2ScopeWebMapperTest {

    @Test
    void toResponseOk() {
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final OAuth2Scope scope = OAuth2Scope.builder()
                .applicationCode(applicationCode)
                .code(code)
                .name(name)
                .description(description)
                .build();

        final OAuth2ScopeResponse expected = OAuth2ScopeResponse.builder()
                .applicationCode(applicationCode)
                .code(code)
                .name(name)
                .description(description)
                .build();

        assertEquals(expected, toResponse(scope));
    }
}