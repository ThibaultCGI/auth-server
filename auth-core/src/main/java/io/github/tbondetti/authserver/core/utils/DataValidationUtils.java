package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import static java.util.Objects.isNull;

@UtilityClass
public class DataValidationUtils {

    public static String normalizeNullableString(final String value) {
        if (isNull(value) || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    public static void validateNullableMaxLength(
            final String value,
            final int maxLength,
            final String errorMessage
    ) {
        if (isNull(value)) {
            return;
        }

        if (value.length() > maxLength) {
            throw new AuthServerFunctionalException(errorMessage);
        }
    }

}
