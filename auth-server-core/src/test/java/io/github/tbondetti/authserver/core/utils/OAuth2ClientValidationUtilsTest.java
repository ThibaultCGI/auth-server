package io.github.tbondetti.authserver.core.utils;

import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import io.github.tbondetti.authserver.core.exception.AuthServerFunctionalException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static io.github.tbondetti.authserver.core.constants.TestConstants.FIFTY_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.TestConstants.TWO_HUNDRED_STRING_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_GRANT_TYPE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_REDIRECT_URI_INVALID;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_REDIRECT_URI_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.REFRESH_TOKEN;
import static io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType.fromValue;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_GRANT_TYPE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_NAME_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_REDIRECT_URI_INVALID;
import static io.github.tbondetti.authserver.core.exception.AuthServerErrorCode.CLIENT_REDIRECT_URI_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeClientId;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeClientName;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeGrantTypes;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeRedirectUris;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.normalizeUri;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateAndNormalizeClientName;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateGrantTypes;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateRedirectUris;
import static io.github.tbondetti.authserver.core.utils.OAuth2ClientValidationUtils.validateUri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

class OAuth2ClientValidationUtilsTest {

    @Test
    void normalizeClientNameOK() {
        assertEquals("aBc", normalizeClientName(" aBc "));
    }


    @Test
    void normalizeClientIdOk() {
        assertEquals("aBc", normalizeClientId(" aBc "));
    }

    @Test
    void validateAndNormalizeClientNameOk() {
        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeClientName(null)
        );

        assertSame(CLIENT_NAME_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_CLIENT_NAME_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateAndNormalizeClientName("   ")
        );

        assertSame(CLIENT_NAME_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_CLIENT_NAME_IS_REQUIRED, exception2.getMessage());

        try (MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(OAuth2ClientValidationUtils.class, CALLS_REAL_METHODS)) {
            final String clientName1 = "clientName1";
            final String normalizedClientName1 = TWO_HUNDRED_STRING_LENGTH
                    + FIFTY_STRING_LENGTH
                    + "123456";

            utilities.when(() -> normalizeClientName(clientName1)).thenReturn(normalizedClientName1); // déjà testé
            final AuthServerFunctionalException exception3 = assertThrows(
                    AuthServerFunctionalException.class,
                    () -> validateAndNormalizeClientName(clientName1)
            );

            assertSame(CLIENT_NAME_IS_TOO_LONG, exception3.getCode());
            assertSame(ERROR_CLIENT_NAME_IS_TOO_LONG, exception3.getMessage());

            final String clientName2 = "clientName2";
            final String normalizedClientName2 = TWO_HUNDRED_STRING_LENGTH
                    + FIFTY_STRING_LENGTH
                    + "12345";

            utilities.when(() -> normalizeClientName(clientName2)).thenReturn(normalizedClientName2); // déjà testé

            assertSame(normalizedClientName2, validateAndNormalizeClientName(clientName2));

        }
    }

    @Test
    void normalizeGrantTypesOk() {
        assertEquals(Set.of(), normalizeGrantTypes(null));
        assertEquals(Set.of(), normalizeGrantTypes(Set.of()));

        final String grantType1 = "grantType1";
        final String grantType2 = "grantType2";
        final String grantType3 = "grantType3";
        final String grantType4 = "grantType1";
        final String grantType5 = "grantType3";

        final Collection<String> grantTypes = List.of(
                grantType1, grantType2, grantType3, grantType4, grantType5
        );

        try (final MockedStatic<OAuth2ClientGrantType> utilities = mockStatic(OAuth2ClientGrantType.class)) {
            utilities.when(() -> fromValue(grantType1)).thenReturn(AUTHORIZATION_CODE);
            utilities.when(() -> fromValue(grantType2)).thenReturn(null);
            utilities.when(() -> fromValue(grantType3)).thenReturn(REFRESH_TOKEN);
            utilities.when(() -> fromValue(grantType4)).thenReturn(AUTHORIZATION_CODE);
            utilities.when(() -> fromValue(grantType5)).thenReturn(REFRESH_TOKEN);

            final Set<OAuth2ClientGrantType> expected = Set.of(
                    AUTHORIZATION_CODE,
                    REFRESH_TOKEN
            );

            assertEquals(expected, normalizeGrantTypes(grantTypes));
        }
    }

    @Test
    void validateGrantTypesOk() {

        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateGrantTypes(null)
        );

        assertSame(CLIENT_GRANT_TYPE_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_CLIENT_GRANT_TYPE_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateGrantTypes(Set.of())
        );

        assertSame(CLIENT_GRANT_TYPE_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_CLIENT_GRANT_TYPE_IS_REQUIRED, exception2.getMessage());

        final AuthServerFunctionalException exception3 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateGrantTypes(Set.of(REFRESH_TOKEN))
        );

        assertSame(
                CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE,
                exception3.getCode()
        );

        assertSame(
                ERROR_CLIENT_GRANT_TYPE_REFRESH_TOKEN_REQUIRES_AUTHORIZATION_CODE,
                exception3.getMessage()
        );

        validateGrantTypes(Set.of(AUTHORIZATION_CODE));
        validateGrantTypes(Set.of(AUTHORIZATION_CODE, REFRESH_TOKEN));
    }

    @Test
    void normalizeUriOk() {

        assertNull(normalizeUri(null));
        assertNull(normalizeUri(" "));
        assertNull(normalizeUri(""));

        final URI uri = URI.create("https://localhost/callback");

        assertEquals(uri, normalizeUri(" https://localhost/callback "));

        final String invalidUri = "http://[invalid";

        final AuthServerFunctionalException exception = assertThrows(
                AuthServerFunctionalException.class,
                () -> normalizeUri(invalidUri)
        );

        assertSame(CLIENT_REDIRECT_URI_INVALID, exception.getCode());
        assertEquals(
                ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(invalidUri),
                exception.getMessage()
        );
    }

    @Test
    void normalizeRedirectUrisOk() {

        assertEquals(Set.of(), normalizeRedirectUris(null));

        final String redirectUri1 = "https://localhost/callback";
        final String redirectUri2 = "https://localhost/callback2";
        final String redirectUri3 = "http://localhost:8080/callback";
        final String redirectUri4 = "http://localhost:8080/callback4";
        final String redirectUri5 = "http://localhost:8080/callback5";

        final List<String> redirectUris = List.of(
                redirectUri1, redirectUri2, redirectUri3, redirectUri4, redirectUri5
        );

        try (final MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(
                OAuth2ClientValidationUtils.class,
                CALLS_REAL_METHODS
        )) {
            final URI uri1 = URI.create("https://test1.com");
            utilities.when(() -> normalizeUri(redirectUri1)).thenReturn(uri1);
            utilities.when(() -> normalizeUri(redirectUri4)).thenReturn(uri1);

            utilities.when(() -> normalizeUri(redirectUri2)).thenReturn(null);

            final URI uri2 = URI.create("https://test2.com");
            utilities.when(() -> normalizeUri(redirectUri3)).thenReturn(uri2);
            utilities.when(() -> normalizeUri(redirectUri5)).thenReturn(uri2);

            final Set<URI> expected = Set.of(uri1, uri2);

            assertEquals(expected, normalizeRedirectUris(redirectUris));
        }
    }

    @Test
    void validateUriOk() {
        validateUri(URI.create("https://localhost/callback"));

        validateUri(URI.create("http://localhost:8080/callback"));

        final URI invalidSchemeUri = URI.create("ftp://localhost/file");

        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateUri(invalidSchemeUri)
        );

        assertSame(CLIENT_REDIRECT_URI_INVALID, exception1.getCode());
        assertEquals(
                ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(invalidSchemeUri),
                exception1.getMessage()
        );

        final URI noHostUri = URI.create("https:///callback");

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateUri(noHostUri)
        );

        assertSame(CLIENT_REDIRECT_URI_INVALID, exception2.getCode());

        assertEquals(
                ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(noHostUri),
                exception2.getMessage()
        );

        final URI fragmentUri = URI.create("https://localhost/callback#fragment");

        final AuthServerFunctionalException exception3 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateUri(fragmentUri)
        );

        assertSame(CLIENT_REDIRECT_URI_INVALID, exception3.getCode());

        assertEquals(
                ERROR_CLIENT_REDIRECT_URI_INVALID.formatted(fragmentUri),
                exception3.getMessage()
        );
    }

    @Test
    void validateRedirectUrisOk() {

        final AuthServerFunctionalException exception1 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateRedirectUris(Set.of(AUTHORIZATION_CODE), null)
        );

        assertSame(CLIENT_REDIRECT_URI_IS_REQUIRED, exception1.getCode());
        assertSame(ERROR_CLIENT_REDIRECT_URI_IS_REQUIRED, exception1.getMessage());

        final AuthServerFunctionalException exception2 = assertThrows(
                AuthServerFunctionalException.class,
                () -> validateRedirectUris(Set.of(AUTHORIZATION_CODE), Set.of())
        );

        assertSame(CLIENT_REDIRECT_URI_IS_REQUIRED, exception2.getCode());
        assertSame(ERROR_CLIENT_REDIRECT_URI_IS_REQUIRED, exception2.getMessage());

        try (final MockedStatic<OAuth2ClientValidationUtils> utilities = mockStatic(
                OAuth2ClientValidationUtils.class,
                CALLS_REAL_METHODS
        )) {

            final URI uri = URI.create("https://localhost/callback");

            utilities.when(() -> validateUri(uri)).thenAnswer(_ -> null);

            validateRedirectUris(Set.of(AUTHORIZATION_CODE), Set.of(uri));

            utilities.verify(() -> validateUri(uri));
        }

        validateRedirectUris(
                Set.of(REFRESH_TOKEN),
                Set.of()
        );

        validateRedirectUris(
                Set.of(REFRESH_TOKEN),
                null
        );
    }
}