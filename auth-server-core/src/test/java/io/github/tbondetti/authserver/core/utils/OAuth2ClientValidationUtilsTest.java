package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static io.github.tbondetti.authserver.core.constants.TestConstants.FIFTY_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.TestConstants.TWO_HUNDRED_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.ERROR_CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.ERROR_CLIENT_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeClientId;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeClientName;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

class OAuth2ClientValidationUtilsTest {

    @Test
    void normalizeClientNameOK() {
        assertEquals("aBc", normalizeClientName(" aBc "));
    }


    @Test
    void normalizeClientIdOk() {
        assertEquals("aBc", normalizeClientId(" aBc "));
    }

    @Test
    void validateAndNormalizeClientNameOk() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeClientName(null)
        );

        assertSame(CLIENT_NAME_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_CLIENT_NAME_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeClientName("   ")
        );

        assertSame(CLIENT_NAME_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_CLIENT_NAME_IS_REQUIRED, exception2.getMessage());

        try (MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(OAuth2ClientValidationUtils.class, CALLS_REAL_METHODS)) {
            final String clientName1 = "clientName1";
            final String normalizedClientName1 = TWO_HUNDRED_STRING_LENGTH
                    + FIFTY_STRING_LENGTH
                    + "123456";

            utilities.when(() -> normalizeClientName(clientName1)).thenReturn(normalizedClientName1); // déjà testé
            final AuthServerFunctionalException exception3 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeClientName(clientName1)
            );

            assertSame(CLIENT_NAME_IS_TOO_LONG, exception3.getCode());
            assertSame(ERROR_CLIENT_NAME_IS_TOO_LONG, exception3.getMessage());

            final String clientName2 = "clientName2";
            final String normalizedClientName2 = TWO_HUNDRED_STRING_LENGTH
                    + FIFTY_STRING_LENGTH
                    + "12345";

            utilities.when(() -> normalizeClientName(clientName2)).thenReturn(normalizedClientName2); // déjà testé

            assertSame(normalizedClientName2, validateAndNormalizeClientName(clientName2));

        }
    }
}