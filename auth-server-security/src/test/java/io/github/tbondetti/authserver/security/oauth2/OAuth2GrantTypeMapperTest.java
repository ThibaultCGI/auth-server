package io.github.tbondetti.authserver.security.oauth2;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.CLIENT_CREDENTIALS;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.REFRESH_TOKEN;
import static io.github.tbondetti.authserver.security.oauth2.OAuth2GrantTypeMapper.toSpring;
import static org.junit.jupiter.api.Assertions.assertSame;

class OAuth2GrantTypeMapperTest {

    @Test
    void toSpringOk() {
        assertSame(AuthorizationGrantType.CLIENT_CREDENTIALS, toSpring(CLIENT_CREDENTIALS));
        assertSame(AuthorizationGrantType.AUTHORIZATION_CODE, toSpring(AUTHORIZATION_CODE));
        assertSame(AuthorizationGrantType.REFRESH_TOKEN, toSpring(REFRESH_TOKEN));
    }
}