package io.github.tbondetti.authserver.core.utils;

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
}
