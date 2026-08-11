package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_GRANT_TYPE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_REDIRECT_URI_INVALID;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_REDIRECT_URI_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.REFRESH_TOKEN;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_GRANT_TYPE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_REDIRECT_URI_INVALID;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_REDIRECT_URI_IS_REQUIRED;
import static java.util.Objects.isNull;

@UtilityClass
public class OAuth2ClientValidationUtils {

    static final String HTTP = "http";
    static final String HTTPS = "https";

    public static String normalizeClientId(final String clientId) {
        return clientId.trim();
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

    public static Set<OAuth2ClientGrantType> normalizeGrantTypes(final Collection<String> grantTypes) {
        if (grantTypes == null || grantTypes.isEmpty()) {
            return Set.of();
        }

        return grantTypes.stream()
                .map(OAuth2ClientGrantType::fromValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                ;
    }

    public static void validateGrantTypes(final Set<OAuth2ClientGrantType> grantTypes) {
        if (grantTypes == null || grantTypes.isEmpty()) {
            throw new AuthServerFunctionalException(CLIENT_GRANT_TYPE_IS_REQUIRED, ERROR_CLIENT_GRANT_TYPE_IS_REQUIRED);
        }

        if (grantTypes.contains(REFRESH_TOKEN) && !grantTypes.contains(AUTHORIZATION_CODE)) {
            throw new AuthServerFunctionalException(
                    CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE,
                    ERROR_CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE
            );
        }
    }

        public static Set<URI> normalizeRedirectUris(final Collection<String> redirectUris) {
        if (isNull(redirectUris)) {
            return Set.of();
        }

        return redirectUris.stream()
                .map(OAuth2ClientValidationUtils::normalizeUri)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                ;
    }

    public static URI normalizeUri(final String uri) {
        if (isNull(uri) || uri.isBlank()) {
            return null;
        }

        final String trimmedUri = uri.trim();
        try {
            return URI.create(trimmedUri);
        }catch (final IllegalArgumentException e) {
            throw new AuthServerFunctionalException(
                    CLIENT_REDIRECT_URI_INVALID,
                    ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(trimmedUri),
                    e
            );
        }
    }

    public static void validateUri(final URI uri) {
        final String scheme = uri.getScheme();

        if (!HTTP.equalsIgnoreCase(scheme) && !HTTPS.equalsIgnoreCase(scheme)) {
            throw new AuthServerFunctionalException(
                    CLIENT_REDIRECT_URI_INVALID,
                    ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(uri)
            );
        }

        if (uri.getHost() == null) {
            throw new AuthServerFunctionalException(
                    CLIENT_REDIRECT_URI_INVALID,
                    ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(uri)
            );
        }

        if (uri.getFragment() != null) {
            throw new AuthServerFunctionalException(
                    CLIENT_REDIRECT_URI_INVALID,
                    ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(uri)
            );
        }
    }

    public static void validateRedirectUris(
            final Set<OAuth2ClientGrantType> grantTypes,
            final Set<URI> uris
    ) {
        if (grantTypes.contains(AUTHORIZATION_CODE) && (uris == null || uris.isEmpty())) {
            throw new AuthServerFunctionalException(CLIENT_REDIRECT_URI_IS_REQUIRED, ERROR_CLIENT_REDIRECT_URI_IS_REQUIRED);
        }

        if (uris == null || uris.isEmpty()) {
            return;
        }

        uris.forEach(OAuth2ClientValidationUtils::validateUri);
    }
}
