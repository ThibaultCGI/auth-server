package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static io.github.tbondetti.authserver.core.constants.TestConstants.TEN_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.TestConstants.USER_NAME;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.ERROR_SECRET_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.ERROR_SECRET_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.ERROR_SECRET_TOO_SHORT;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.ERROR_USERNAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.ERROR_USERNAME_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.normalizeUsername;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.validateAndNormalizeUsername;
import static io.github.tbondetti.authserver.core.utils.UserValidationUtils.validatePassword;
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

class UserValidationUtilsTest {

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword(null)
        );

        assertEquals(ERROR_SECRET_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword("   ")
        );

        assertEquals(ERROR_SECRET_IS_REQUIRED, exception2.getMessage());

        final AuthServerFunctionalException exception3 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword("12345678901")
        );

        assertEquals(ERROR_SECRET_TOO_SHORT, exception3.getMessage());

        final AuthServerFunctionalException exception4 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validatePassword(
                        TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                TEN_STRING_LENGTH +
                                "123456789"
                )
        );

        assertEquals(ERROR_SECRET_TOO_LONG, exception4.getMessage());
    }

    @Test
    void normalizeUsernameOk() {
        assertEquals("user_name", normalizeUsername("  " + USER_NAME + "  "));
    }

    @Test
    void shouldNormalizeUsernameWhenUsernameIsValid() {
        assertEquals(USER_NAME.toLowerCase(ROOT), validateAndNormalizeUsername("   " + USER_NAME + "  "));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsInvalid() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeUsername(null)
        );

        assertEquals(ERROR_USERNAME_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeUsername("   ")
        );

        assertEquals(ERROR_USERNAME_IS_REQUIRED, exception2.getMessage());

        final String string101 = TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                TEN_STRING_LENGTH +
                "1";

        try (MockedStatic<UserValidationUtils> userUtilities = mockStatic(UserValidationUtils.class, CALLS_REAL_METHODS)) {
            userUtilities.when(() -> normalizeUsername(USER_NAME)).thenReturn(string101); // déjà testé

            final AuthServerFunctionalException exception3 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeUsername(USER_NAME)
            );

            assertEquals(ERROR_USERNAME_TOO_LONG, exception3.getMessage());
        }
    }

}