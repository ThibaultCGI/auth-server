package io.github.tbondetti.authserver.core.enums;

import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.stream.Stream;

import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_GRANT_TYPE_INVALID;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_GRANT_TYPE_INVALID;

@Getter
@AllArgsConstructor
public enum OAuth2ClientGrantType {

    CLIENT_CREDENTIALS("client_credentials"),
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token")
    ;

    private final String value;

    public static OAuth2ClientGrantType fromValue(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        final String formattedValue = value.trim().toLowerCase(Locale.ROOT);

        return Stream.of(values())
                .filter(t -> t.value.equals(formattedValue))
                .findFirst()
                .orElseThrow(() -> new AuthServerFunctionalException(CLIENT_GRANT_TYPE_INVALID, ERROR_CLIENT_GRANT_TYPE_INVALID.formatted(formattedValue)))
                ;
    }
}
