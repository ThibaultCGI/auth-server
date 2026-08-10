package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CODE_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_DESCRIPTION_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_NAME_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.DESCRIPTION_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.NAME_IS_TOO_LONG;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;

@UtilityClass
public class CommonValidationUtils {

    public static String normalizeCode(final String code) {
        return code.trim().toUpperCase(ROOT);
    }

    public static String validateAndNormalizeCode(
            final String code,
            final int maxLength
    ) {
        if (isNull(code) || code.isBlank()) {
            throw new AuthServerFunctionalException(CODE_IS_REQUIRED, ERROR_CODE_IS_REQUIRED);
        }

        final String normalizedCode = normalizeCode(code);

        if (normalizedCode.length() > maxLength) {
            throw new AuthServerFunctionalException(CODE_IS_TOO_LONG, ERROR_CODE_TOO_LONG.formatted(maxLength));
        }

        return normalizedCode;
    }

    public static String normalizeNullableString(final String value) {
        if (isNull(value) || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    public static String normalizeAndValidateDescription(
            final String description,
            final int maxLength
    ) {
        final String normalizedDescription = normalizeNullableString(description);

        if (isNull(normalizedDescription)) {
            return null;
        }

        if (normalizedDescription.length() > maxLength) {
            throw new AuthServerFunctionalException(DESCRIPTION_IS_TOO_LONG, ERROR_DESCRIPTION_TOO_LONG.formatted(maxLength));
        }

        return normalizedDescription;
    }

    public static String validateAndNormalizeName(
            final String name,
            final int maxLength
    ) {
        if (isNull(name) || name.isBlank()) {
            throw new AuthServerFunctionalException(NAME_IS_REQUIRED, ERROR_NAME_IS_REQUIRED);
        }

        final String normalizedName = name.trim();

        if (normalizedName.length() > maxLength) {
            throw new AuthServerFunctionalException(NAME_IS_TOO_LONG, ERROR_NAME_TOO_LONG.formatted(maxLength));
        }

        return normalizedName;
    }



}
