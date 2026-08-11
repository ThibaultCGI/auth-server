package io.github.tbondetti.authserver.core.enums;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_GRANT_TYPE_INVALID;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.CLIENT_CREDENTIALS;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.REFRESH_TOKEN;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.fromValue;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_GRANT_TYPE_INVALID;
import static org.junit.jupiter.api.Assertions.*;

class OAuth2ClientGrantTypeTest {

    @Test
    void fromValueOk() {
        assertNull(fromValue(null));
        assertNull(fromValue("   "));

        assertEquals(CLIENT_CREDENTIALS, fromValue("   " + CLIENT_CREDENTIALS.getValue().toUpperCase(Locale.ROOT) + "   "));
        assertEquals(AUTHORIZATION_CODE, fromValue("   " + AUTHORIZATION_CODE.getValue().toUpperCase(Locale.ROOT) + "   "));
        assertEquals(REFRESH_TOKEN, fromValue("   " + REFRESH_TOKEN.getValue().toUpperCase(Locale.ROOT) + "   "));


        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> fromValue(" auTRe ")
        );

        assertSame(CLIENT_GRANT_TYPE_INVALID, exception.getCode());
        assertEquals(ERROR_CLIENT_GRANT_TYPE_INVALID.formatted("autre"), exception.getMessage());
    }
}