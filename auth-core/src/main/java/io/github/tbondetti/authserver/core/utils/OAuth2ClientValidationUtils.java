package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_TOO_LONG;
import static java.util.Objects.isNull;

@UtilityClass
public class OAuth2ClientValidationUtils {
    static final String ERROR_CLIENT_NAME_IS_REQUIRED = "Le nom du client est obligatoire.";
    static final String ERROR_CLIENT_NAME_IS_TOO_LONG = "Le nom du client ne doit pas dépasser les 255 caractères.";

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

}
