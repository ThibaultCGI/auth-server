package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import static io.github.tbondetti.authserver.core.constants.UserRules.PASSWORD_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.UserRules.PASSWORD_MIN_LENGTH;
import static io.github.tbondetti.authserver.core.constants.UserRules.USERNAME_MAX_LENGTH;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;

@UtilityClass
public class UserValidationUtils {
    static final String ERROR_USERNAME_IS_REQUIRED = "Le username est obligatoire.";
    static final String ERROR_USERNAME_TOO_LONG = "Le username ne doit pas dépasser les 100 caractères.";

    static final String ERROR_SECRET_IS_REQUIRED = "Le mot de passe est obligatoire et ne peut pas être vide.";
    static final String ERROR_SECRET_TOO_SHORT = "Le mot de passe doit contenir au minimum 12 caractères.";
    static final String ERROR_SECRET_TOO_LONG = "Le mot de passe ne doit pas dépasser les 128 caractères.";


    public static String validateAndNormalizeUsername(final String username) {
        if (isNull(username) || username.isBlank()) {
            throw new AuthServerFunctionalException(ERROR_USERNAME_IS_REQUIRED);
        }

        final String normalizedUsername = normalizeUsername(username);

        if (normalizedUsername.length() > USERNAME_MAX_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_USERNAME_TOO_LONG);
        }

        return normalizedUsername;
    }

    static String normalizeUsername(final String username) {
        return username.trim().toLowerCase(ROOT);
    }

    public static void validatePassword(final String password) {
        if (isNull(password) || password.isBlank()) {
            throw new AuthServerFunctionalException(ERROR_SECRET_IS_REQUIRED);
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_SECRET_TOO_SHORT);
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new AuthServerFunctionalException(ERROR_SECRET_TOO_LONG);
        }
    }
}
