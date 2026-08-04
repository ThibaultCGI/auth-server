package io.github.tbondetti.authserver.web.api.v1.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import io.github.tbondetti.authserver.web.api.v1.response.OAuth2ScopeResponse;
import org.junit.jupiter.api.Test;

import static io.github.tbondetti.authserver.web.api.v1.mapper.OAuth2ScopeWebMapper.toResponse;
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