package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.ERROR_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.ERROR_CODE_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.ERROR_DESCRIPTION_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.ERROR_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.ERROR_NAME_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeCode;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeNullableString;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.validateAndNormalizeName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

class CommonValidationUtilsTest {

    @Test
    void normalizeCodeOk() {
        assertEquals("CODE", normalizeCode("   coDe   "));
    }

    @Test
    void shouldCodeNotBeBlank() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeCode(null, 10)
        );
        assertEquals(ERROR_CODE_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeCode("    ", 10)
        );
        assertEquals(ERROR_CODE_IS_REQUIRED, exception2.getMessage());
    }

    @Test
    void shouldCodeLengthNotBeTooLong() {
        final String givenCode = "givenCode";

        final String normalizedCode = "12345678901";

        try(final MockedStatic<CommonValidationUtils> userUtilities = mockStatic(CommonValidationUtils.class, CALLS_REAL_METHODS)) {
            userUtilities.when(() -> normalizeCode(givenCode)).thenReturn(normalizedCode); // déjà testé

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeCode(givenCode, 10)
            );
            assertEquals(ERROR_CODE_TOO_LONG.formatted(10), exception.getMessage());
        }
    }

    @Test
    void validateAndNormalizeCodeOk() {
        final String givenCode = "givenCode";

        final String normalizedCode = "1234567890";

        try(final MockedStatic<CommonValidationUtils> userUtilities = mockStatic(CommonValidationUtils.class, CALLS_REAL_METHODS)) {
            userUtilities.when(() -> normalizeCode(givenCode)).thenReturn(normalizedCode); // déjà testé

            assertSame(normalizedCode, validateAndNormalizeCode(givenCode, 10));
        }
    }

    @Test
    void normalizeNullableStringOk() {
        assertNull(normalizeNullableString(null));
        assertNull(normalizeNullableString("      "));
        assertEquals("aBc",  normalizeNullableString("  aBc    "));
    }

    @Test
    void normalizeAndValidateDescriptionNull() {
        final String givenDescription = "givenDescription";
        final int givenMaxLength = 10;

        try(final MockedStatic<CommonValidationUtils> userUtilities = mockStatic(CommonValidationUtils.class, CALLS_REAL_METHODS)) {
            userUtilities.when(() -> normalizeNullableString(givenDescription)).thenReturn(null); // déjà testé

            assertNull(normalizeAndValidateDescription(givenDescription, givenMaxLength));
        }
    }

    @Test
    void normalizeAndValidateDescriptionTooLong() {
        final String givenDescription = "givenDescription";
        final int givenMaxLength = 10;

        final String normalizedDescription = "12345678901";
        try(final MockedStatic<CommonValidationUtils> userUtilities = mockStatic(CommonValidationUtils.class, CALLS_REAL_METHODS)) {
            userUtilities.when(() -> normalizeNullableString(givenDescription)).thenReturn(normalizedDescription); // déjà testé

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> normalizeAndValidateDescription(givenDescription, givenMaxLength)
            );
            assertEquals(ERROR_DESCRIPTION_TOO_LONG.formatted(givenMaxLength), exception.getMessage());
        }
    }

    @Test
    void normalizeAndValidateDescriptionNotNull() {
        final String givenDescription = "givenDescription";
        final int givenMaxLength = 10;

        final String normalizedDescription = "1234567890";

        try(final MockedStatic<CommonValidationUtils> userUtilities = mockStatic(CommonValidationUtils.class, CALLS_REAL_METHODS)) {
            userUtilities.when(() -> normalizeNullableString(givenDescription)).thenReturn(normalizedDescription); // déjà testé

            assertSame(normalizedDescription, normalizeAndValidateDescription(givenDescription, givenMaxLength));
        }
    }

    @Test
    void shouldNameNotBeBlank() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName(null, 10)
        );
        assertEquals(ERROR_NAME_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName("    ", 10)
        );
        assertEquals(ERROR_NAME_IS_REQUIRED, exception2.getMessage());
    }

    @Test
    void shouldNameNotBeTooLong() {
        final String givenName = "   12345678901   ";
        final int givenMaxLength = 10;

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName(givenName, givenMaxLength)
        );
        assertEquals(ERROR_NAME_TOO_LONG.formatted(givenMaxLength), exception2.getMessage());
    }

    @Test
    void validateAndNormalizeNameOk() {
        final String givenName = "   1234567890   ";
        final int givenMaxLength = 10;

        assertEquals("1234567890", validateAndNormalizeName(givenName, givenMaxLength));
    }
}