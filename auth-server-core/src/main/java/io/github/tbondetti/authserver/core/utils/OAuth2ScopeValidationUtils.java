package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_PATTERN;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_HAS_INVALID_CARACTER;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_DESCRIPTION_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.SCOPE_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.utils.CommonValidationUtils.normalizeNullableString;
import static java.util.Objects.isNull;

@UtilityClass
public class OAuth2ScopeValidationUtils {
    static final String ERROR_CODE_IS_REQUIRED = "Le code du scope est obligatoire.";
    static final String ERROR_CODE_TOO_LONG = "Le code du scope ne doit pas dépasser les 100 caractères.";
    static final String ERROR_CODE_HAS_INVALID_CARACTER = "Le code du scope ne peut contenir que des lettres "
            + "minuscules, des points (.) et des tirets (-).";
    static final String ERROR_NAME_IS_REQUIRED = "Le nom du scope est obligatoire.";
    static final String ERROR_NAME_TOO_LONG = "Le nom du scope ne doit pas dépasser les 100 caractères.";

    static final String ERROR_DESCRIPTION_TOO_LONG = "Le nom du scope ne doit pas dépasser les 500 caractères.";


    public static String normalizeCode(final String code) {
        return code.trim().toLowerCase();
    }


    public static String validateAndNormalizeCode(final String code) {
        if (isNull(code) || code.isBlank()) {
            throw new AuthServerFunctionalException(
                    SCOPE_CODE_IS_REQUIRED,
                    ERROR_CODE_IS_REQUIRED
            );
        }

        final String normalizedCode = normalizeCode(code);

        if (normalizedCode.length() > SCOPE_CODE_MAX_LENGTH) {
            throw new AuthServerFunctionalException(SCOPE_CODE_IS_TOO_LONG, ERROR_CODE_TOO_LONG);
        }

        if (!Pattern.matches(SCOPE_CODE_PATTERN, normalizedCode)) {
            throw new AuthServerFunctionalException(SCOPE_CODE_HAS_INVALID_CARACTER, ERROR_CODE_HAS_INVALID_CARACTER);
        }

        return normalizedCode;
    }

    public static String validateAndNormalizeName(final String name) {
        if (isNull(name) || name.isBlank()) {
            throw new AuthServerFunctionalException(SCOPE_NAME_IS_REQUIRED, ERROR_NAME_IS_REQUIRED);
        }

        final String normalizedName = normalizeName(name);

        if (normalizedName.length() > SCOPE_NAME_MAX_LENGTH) {
            throw new AuthServerFunctionalException(SCOPE_NAME_IS_TOO_LONG, ERROR_NAME_TOO_LONG);
        }

        return normalizedName;
    }

    public static String normalizeName(final String name) {
        return name.trim();
    }

    public static String normalizeAndValidateDescription(final String description) {
        final String normalizedDescription = normalizeNullableString(description);
        if (isNull(normalizedDescription)) {
            return null;
        }

        if (normalizedDescription.length() > SCOPE_DESCRIPTION_MAX_LENGTH) {
            throw new AuthServerFunctionalException(SCOPE_DESCRIPTION_IS_TOO_LONG, ERROR_DESCRIPTION_TOO_LONG);
        }

        return normalizedDescription;
    }
}
