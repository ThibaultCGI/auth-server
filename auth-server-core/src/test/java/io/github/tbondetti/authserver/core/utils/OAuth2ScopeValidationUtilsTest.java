package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static io.github.tbondetti.authserver.core.constants.TestConstants.ONE_HUNDRED_NON_NUMERIC_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.TestConstants.ONE_HUNDRED_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.TestConstants.TWO_HUNDRED_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_CODE_HAS_INVALID_CARACTER;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_CODE_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_DESCRIPTION_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_NAME_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_HAS_INVALID_CARACTER;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_DESCRIPTION_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeNullableString;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.normalizeAndValidateDescription;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.normalizeCode;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.normalizeName;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.validateAndNormalizeCode;
import static io.github.tbondetti.authserver.core.utils.OAuth2ScopeValidationUtils.validateAndNormalizeName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

class OAuth2ScopeValidationUtilsTest {

    @Test
    void normalizeCodeOk() {
        assertEquals("abc", normalizeCode(" AbC "));
    }

    @Test
    void validateAndNormalizeCodeOk() {

        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeCode(null)
        );

        assertSame(SCOPE_CODE_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_SCOPE_CODE_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeCode("   ")
        );

        assertSame(SCOPE_CODE_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_SCOPE_CODE_IS_REQUIRED, exception2.getMessage());

        try (MockedStatic<OAuth2ScopeValidationUtils> utilities = mockStatic(OAuth2ScopeValidationUtils.class, CALLS_REAL_METHODS)) {
            final String code1 = "code1";
            final String normalizedCode1 = ONE_HUNDRED_STRING_LENGTH + "1";

            utilities.when(() -> normalizeCode(code1)).thenReturn(normalizedCode1); // déjà testé

            final AuthServerFunctionalException exception3 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeCode(code1)
            );

            assertSame(SCOPE_CODE_IS_TOO_LONG, exception3.getCode());
            assertSame(ERROR_SCOPE_CODE_TOO_LONG, exception3.getMessage());

            final String code2 = "code2";
            final String normalizedCode2 = "normalizedCode2";

            utilities.when(() -> normalizeCode(code2)).thenReturn(normalizedCode2); // déjà testé

            final AuthServerFunctionalException exception4 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeCode(code2)
            );

            assertSame(SCOPE_CODE_HAS_INVALID_CARACTER, exception4.getCode());
            assertEquals(ERROR_SCOPE_CODE_HAS_INVALID_CARACTER, exception4.getMessage());

            final String code3 = "code3";

            utilities.when(() -> normalizeCode(code3)).thenReturn(ONE_HUNDRED_NON_NUMERIC_STRING_LENGTH); // déjà testé

            assertSame(ONE_HUNDRED_NON_NUMERIC_STRING_LENGTH, validateAndNormalizeCode(code3));
        }
    }

    @Test
    void normalizeNameOk() {
        assertEquals("AbC", normalizeName(" AbC "));
    }

    @Test
    void validateAndNormalizeNameOk() {

        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName(null)
        );

        assertSame(SCOPE_NAME_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_SCOPE_NAME_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeName("   ")
        );

        assertSame(SCOPE_NAME_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_SCOPE_NAME_IS_REQUIRED, exception2.getMessage());

        try (MockedStatic<OAuth2ScopeValidationUtils> utilities = mockStatic(OAuth2ScopeValidationUtils.class, CALLS_REAL_METHODS)) {
            final String name1 = "name1";

            final String normalizedName1 = ONE_HUNDRED_STRING_LENGTH + "1";

            utilities.when(() -> normalizeName(name1)).thenReturn(normalizedName1); // déjà testé

            final AuthServerFunctionalException exception3 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeName(name1)
            );

            assertSame(SCOPE_NAME_IS_TOO_LONG, exception3.getCode());
            assertSame(ERROR_SCOPE_NAME_TOO_LONG, exception3.getMessage());

            final String name2 = "name2";

            final String normalizedName2 = ONE_HUNDRED_STRING_LENGTH;

            utilities.when(() -> normalizeName(name2)).thenReturn(normalizedName2); // déjà testé

            assertSame(normalizedName2, validateAndNormalizeName(name2));
        }
    }

    @Test
    void normalizeAndValidateDescriptionOk() {
        try (final MockedStatic<CommonValidationUtils> commonUtilities = mockStatic(
                CommonValidationUtils.class,
                CALLS_REAL_METHODS
        )) {
            final String description0 = "description0";
            commonUtilities.when(() -> normalizeNullableString(description0)).thenReturn(null);

            assertNull(normalizeAndValidateDescription(description0));

            final String description1 = "description1";

            final String normalizedDescription1 = TWO_HUNDRED_STRING_LENGTH
                    + TWO_HUNDRED_STRING_LENGTH
                    + ONE_HUNDRED_STRING_LENGTH
                    + "1"
                    ;

            commonUtilities.when(() -> normalizeNullableString(description1))
                    .thenReturn(normalizedDescription1); // déjà testé

            final AuthServerFunctionalException exception = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> normalizeAndValidateDescription(description1)
            );

            assertSame(SCOPE_DESCRIPTION_IS_TOO_LONG, exception.getCode());
            assertSame(ERROR_SCOPE_DESCRIPTION_TOO_LONG, exception.getMessage());

            final String description2 = "description2";

            final String normalizedDescription2 = TWO_HUNDRED_STRING_LENGTH
                    + TWO_HUNDRED_STRING_LENGTH
                    + ONE_HUNDRED_STRING_LENGTH
                    ;

            commonUtilities.when(() -> normalizeNullableString(description2))
                    .thenReturn(normalizedDescription2); // déjà testé

            assertSame(normalizedDescription2, normalizeAndValidateDescription(description2));
        }

    }
}