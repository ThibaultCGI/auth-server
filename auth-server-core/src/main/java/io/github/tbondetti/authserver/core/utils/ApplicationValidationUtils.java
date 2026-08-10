package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

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
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeNullableString;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;

@UtilityClass
public class ApplicationValidationUtils {

    public static String normalizeCode(final String code) {
        return code.trim().toUpperCase(ROOT);
    }

    public static String validateAndNormalizeCode(final String code) {
        if (isNull(code) || code.isBlank()) {
            throw new AuthServerFunctionalException(APPLICATION_CODE_IS_REQUIRED, ERROR_APPLICATION_CODE_IS_REQUIRED);
        }

        final String normalizedCode = normalizeCode(code);

        if (normalizedCode.length() > APPLICATION_CODE_MAX_LENGTH) {
            throw new AuthServerFunctionalException(APPLICATION_CODE_IS_TOO_LONG, ERROR_APPLICATION_CODE_IS_TOO_LONG);
        }

        return normalizedCode;
    }

    public static String normalizeAndValidateDescription(final String description) {
        final String normalizedDescription = normalizeNullableString(description);

        if (isNull(normalizedDescription)) {
            return null;
        }

        if (normalizedDescription.length() > APPLICATION_DESCRIPTION_MAX_LENGTH) {
            throw new AuthServerFunctionalException(DESCRIPTION_IS_TOO_LONG, ERROR_APPLICATION_DESCRIPTION_IS_TOO_LONG);
        }

        return normalizedDescription;
    }

    public static String validateAndNormalizeName(
            final String name
    ) {
        if (isNull(name) || name.isBlank()) {
            throw new AuthServerFunctionalException(APPLICATION_NAME_IS_REQUIRED, ERROR_APPLICATION_NAME_IS_REQUIRED);
        }

        final String normalizedName = name.trim();

        if (normalizedName.length() > APPLICATION_NAME_MAX_LENGTH) {
            throw new AuthServerFunctionalException(APPLICATION_NAME_IS_TOO_LONG, ERROR_APPLICATION_NAME_IS_TOO_LONG);
        }

        return normalizedName;
    }



}
