package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_DESCRIPTION_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.APPLICATION_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.APPLICATION_CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.APPLICATION_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.APPLICATION_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.DESCRIPTION_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.normalizeCode;
import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.ApplicationValidationUtils.validateAndNormalizeName;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeNullableString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

class ApplicationValidationUtilsTest {

    @Test
    void normalizeCodeOk() {
        assertEquals("CODE", normalizeCode("   cOdE   "));
    }

    @Test
    void validateAndNormalizeCodeKo() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeCode(null)
        );

        assertSame(APPLICATION_CODE_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_APPLICATION_CODE_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeCode("     ")
        );

        assertSame(APPLICATION_CODE_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_APPLICATION_CODE_IS_REQUIRED, exception2.getMessage());

        final String code = "code";
        try (final MockedStatic<ApplicationValidationUtils> utilities = mockStatic(ApplicationValidationUtils.class, CALLS_REAL_METHODS)) {
            final String normalizedCode = "n".repeat(APPLICATION_CODE_MAX_LENGTH + 1);
            utilities.when(() -> normalizeCode(code)).thenReturn(normalizedCode);

            final AuthServerFunctionalException exception3 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeCode(code)
            );

            assertSame(APPLICATION_CODE_IS_TOO_LONG, exception3.getCode());
            assertSame(ERROR_APPLICATION_CODE_IS_TOO_LONG, exception3.getMessage());
        }
    }

    @Test
    void validateAndNormalizeCodeOk() {
        final String code = "code";
        try (final MockedStatic<ApplicationValidationUtils> utilities = mockStatic(ApplicationValidationUtils.class, CALLS_REAL_METHODS)) {
            final String normalizedCode = "n".repeat(APPLICATION_CODE_MAX_LENGTH);
            utilities.when(() -> normalizeCode(code)).thenReturn(normalizedCode);

            assertSame(normalizedCode, validateAndNormalizeCode(code));
        }
    }

    @Test
    void normalizeAndValidateDescriptionNull() {
        final String description = "description";
        try (final MockedStatic<CommonValidationUtils> utilities = mockStatic(CommonValidationUtils.class)) {
            utilities.when(() -> normalizeNullableString(description)).thenReturn(null);
            assertNull(normalizeAndValidateDescription(description));
        }
    }

    @Test
    void normalizeAndValidateDescriptionKo() {
        final String description = "description";
        try (final MockedStatic<CommonValidationUtils> utilities = mockStatic(CommonValidationUtils.class)) {
            final String normalizedDescription = "d".repeat(APPLICATION_DESCRIPTION_MAX_LENGTH + 1);
            utilities.when(() -> normalizeNullableString(description)).thenReturn(normalizedDescription);

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> normalizeAndValidateDescription(description)
            );

            assertSame(DESCRIPTION_IS_TOO_LONG, exception.getCode());
            assertSame(ERROR_APPLICATION_DESCRIPTION_IS_TOO_LONG, exception.getMessage());
        }
    }

    @Test
    void normalizeAndValidateDescriptionOk() {
        final String description = "description";
        try (final MockedStatic<CommonValidationUtils> utilities = mockStatic(CommonValidationUtils.class)) {
            final String normalizedDescription = "d".repeat(APPLICATION_DESCRIPTION_MAX_LENGTH);
            utilities.when(() -> normalizeNullableString(description)).thenReturn(normalizedDescription);

            assertSame(normalizedDescription, normalizeAndValidateDescription(description));
        }
    }

    @Test
    void validateAndNormalizeNameKo() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName(null)
        );

        assertSame(APPLICATION_NAME_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_APPLICATION_NAME_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName("   ")
        );

        assertSame(APPLICATION_NAME_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_APPLICATION_NAME_IS_REQUIRED, exception2.getMessage());

        final String name = "  " + "n".repeat(APPLICATION_NAME_MAX_LENGTH + 1) + "  ";
        final AuthServerFunctionalException exception3 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName(name)
        );

        assertSame(APPLICATION_NAME_IS_TOO_LONG, exception3.getCode());
        assertSame(ERROR_APPLICATION_NAME_IS_TOO_LONG, exception3.getMessage());
    }

    @Test
    void validateAndNormalizeNameOk() {
        final String name = "  " + "n".repeat(APPLICATION_NAME_MAX_LENGTH) + "  ";

        assertEquals("n".repeat(APPLICATION_NAME_MAX_LENGTH), validateAndNormalizeName(name));
    }
}