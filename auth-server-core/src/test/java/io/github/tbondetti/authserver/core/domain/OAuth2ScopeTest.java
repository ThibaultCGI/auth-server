package io.github.tbondetti.authserver.core.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.tbondetti.authserver.core.domain.OAuth2Scope.OPENID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class OAuth2ScopeTest {

    @Test
    void completeCodeOk() {
        final UUID id = UUID.randomUUID();
        final String applicationCode = "applicationCode";
        final String code = "code";
        final String name = "name";
        final String description = "description";

        final OAuth2Scope scope1 = OAuth2Scope.builder()
                .id(id)
                .applicationCode(applicationCode)
                .code(code)
                .name(name)
                .description(description)
                .build();

        assertEquals("applicationcode:code", scope1.completeCode());

        final String openid = "openid";
        final OAuth2Scope scope2 = OAuth2Scope.builder()
                .id(id)
                .applicationCode(applicationCode)
                .code(openid)
                .name(name)
                .description(description)
                .build();

        assertSame(OPENID, scope2.completeCode());


    }
}