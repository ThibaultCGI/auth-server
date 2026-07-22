package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_ID_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_SECRET_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_SECRET_MIN_LENGTH;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_ID_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_ID_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_SECRET_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_SECRET_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_SECRET_IS_TOO_SHORT;
import static java.util.Objects.isNull;

@UtilityClass
public class OAuth2ClientValidationUtils {
    static final String ERROR_CLIENT_ID_IS_REQUIRED = "Le client ID est obligatoire.";
    static final String ERROR_CLIENT_ID_IS_TOO_LONG = "Le client ID ne doit pas dépasser les 100 caractères.";

    static final String ERROR_CLIENT_NAME_IS_REQUIRED = "Le nom du client est obligatoire.";
    static final String ERROR_CLIENT_NAME_IS_TOO_LONG = "Le nom du client ne doit pas dépasser les 255 caractères.";

    static final String ERROR_SECRET_IS_REQUIRED = "Le client secret est obligatoire et ne peut pas être vide.";
    static final String ERROR_SECRET_TOO_SHORT = "Le client secret doit contenir au minimum 5 caractères.";
    static final String ERROR_SECRET_TOO_LONG = "Le client secret ne doit pas dépasser les 128 caractères.";

    public static String validateAndNormalizeClientId(final String clientId) {
        if (isNull(clientId) || clientId.isBlank()) {
            throw new AuthServerFunctionalException(CLIENT_ID_IS_REQUIRED, ERROR_CLIENT_ID_IS_REQUIRED);
        }

        final String normalizedClientId = normalizeClientId(clientId);

        if (normalizedClientId.length() > CLIENT_ID_MAX_LENGTH) {
            throw new AuthServerFunctionalException(CLIENT_ID_IS_TOO_LONG, ERROR_CLIENT_ID_IS_TOO_LONG);
        }

        return normalizedClientId;
    }

    public static String normalizeClientId(final String clientId) {
        return clientId.trim().toLowerCase();
    }

    public static String validateAndNormalizeClientName(final String clientName) {
        if (isNull(clientName) || clientName.isBlank()) {
            throw new AuthServerFunctionalException(CLIENT_NAME_IS_REQUIRED, ERROR_CLIENT_NAME_IS_REQUIRED);
        }

        final String normalizedClientName = normalizeClientName(clientName);

        if (normalizedClientName.length() > CLIENT_NAME_MAX_LENGTH) {
            throw new AuthServerFunctionalException(CLIENT_NAME_IS_TOO_LONG, ERROR_CLIENT_NAME_IS_TOO_LONG);
        }

        return normalizedClientName;
    }

    public static String normalizeClientName(final String clientName) {
        return clientName.trim();
    }

    public static void validateClientSecret(final String clientSecret) {
        if (isNull(clientSecret) || clientSecret.isBlank()) {
            throw new AuthServerFunctionalException(CLIENT_SECRET_IS_REQUIRED, ERROR_SECRET_IS_REQUIRED);
        }

        if (clientSecret.length() < CLIENT_SECRET_MIN_LENGTH) {
            throw new AuthServerFunctionalException(CLIENT_SECRET_IS_TOO_SHORT, ERROR_SECRET_TOO_SHORT);
        }

        if (clientSecret.length() > CLIENT_SECRET_MAX_LENGTH) {
            throw new AuthServerFunctionalException(CLIENT_SECRET_IS_TOO_LONG, ERROR_SECRET_TOO_LONG);
        }
    }
}
