package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.NAME_MAX_LENGTH;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;

@UtilityClass
public class ApplicationValidationUtils {
    static final String ERROR_CODE_IS_REQUIRED = "Le code est obligatoire.";
    static final String ERROR_CODE_TOO_LONG = "Le code ne doit pas dépasser les 100 caractères.";

    static final String ERROR_NAME_IS_REQUIRED = "Le nom est obligatoire et ne peut pas être vide.";
    static final String ERROR_NAME_TOO_LONG = "Le nom ne doit pas dépasser les 100 caractères.";


    public static String validateAndNormalizeCode(final String code) {
        if (isNull(code) || code.isBlank()) {
            throw new AuthServerFunctionalException(ERROR_CODE_IS_REQUIRED);
        }

        final String normalizedCode = normalizeCode(code);

        if (normalizedCode.length() > CODE_MAX_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_CODE_TOO_LONG);
        }

        return normalizedCode;
    }

    public static String normalizeCode(final String code) {
        return code.trim().toUpperCase(ROOT);
    }

    public static String validateAndNormalizeName(final String name) {
        if (isNull(name) || name.isBlank()) {
            throw new AuthServerFunctionalException(ERROR_NAME_IS_REQUIRED);
        }

        final String normalizedName = name.trim();

        if (normalizedName.length() > NAME_MAX_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_NAME_TOO_LONG);
        }

        return normalizedName;
    }

    public static String normalizeDescription(final String description) {
        if (isNull(description) || description.isBlank()) {
            return null;
        }

        return description.trim();
    }
}
