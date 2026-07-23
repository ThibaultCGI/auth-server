package io.github.tbondetti.authserver.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.tbondetti.authserver.infrastructure.security.OAuth2ClientCredentialsGeneratorAdapter.ALPHABET;
import static io.github.tbondetti.authserver.infrastructure.security.OAuth2ClientCredentialsGeneratorAdapter.CLIENT_ID_SEQUENCE_COUNT;
import static io.github.tbondetti.authserver.infrastructure.security.OAuth2ClientCredentialsGeneratorAdapter.CLIENT_ID_SEQUENCE_LENGTH;
import static io.github.tbondetti.authserver.infrastructure.security.OAuth2ClientCredentialsGeneratorAdapter.CLIENT_ID_SEQUENCE_SEPARATOR;
import static io.github.tbondetti.authserver.infrastructure.security.OAuth2ClientCredentialsGeneratorAdapter.CLIENT_SECRET_LENGTH;
import static io.github.tbondetti.authserver.infrastructure.security.OAuth2ClientCredentialsGeneratorAdapter.generateRandomString;
import static io.github.tbondetti.authserver.infrastructure.security.OAuth2ClientCredentialsGeneratorAdapter.generatedClientIdLength;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OAuth2ClientCredentialsGeneratorAdapterTest {

    @InjectMocks
    private OAuth2ClientCredentialsGeneratorAdapter subject;

    @Test
    void generatedClientIdLengthOK() {
        final int expected = CLIENT_ID_SEQUENCE_LENGTH * CLIENT_ID_SEQUENCE_COUNT
                + (CLIENT_ID_SEQUENCE_COUNT - 1) * CLIENT_ID_SEQUENCE_SEPARATOR.length();
        assertEquals(expected, generatedClientIdLength());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1,
            3,
            5,
            CLIENT_ID_SEQUENCE_LENGTH,
            15,
            30,
            CLIENT_SECRET_LENGTH
    })
    void generateRandomStringOK(final int sequenceLength) {
        final String generated = generateRandomString(sequenceLength);
        assertEquals(sequenceLength, generated.length());

        for (int i = 0; i < generated.length(); i++) {
            assertTrue(ALPHABET.contains(String.valueOf(generated.charAt(i))));
        }
    }

    @Test
    void generateClientSecretOk() {
        try(final MockedStatic<OAuth2ClientCredentialsGeneratorAdapter> utilities = mockStatic(OAuth2ClientCredentialsGeneratorAdapter.class, CALLS_REAL_METHODS)) {
            final String clientSecret = "clientSecret";
            utilities.when(() -> generateRandomString(CLIENT_SECRET_LENGTH)).thenReturn(clientSecret); // déjà testé

            assertSame(clientSecret, this.subject.generateClientSecret());

            utilities.verify(
                    () -> generateRandomString(CLIENT_SECRET_LENGTH),
                    times(1)
            );
        }
    }

    @Test
    void generateClientIdOk() {
        final String randomSequence = "123456";
        final String expected = randomSequence
                + CLIENT_ID_SEQUENCE_SEPARATOR + randomSequence
                + CLIENT_ID_SEQUENCE_SEPARATOR + randomSequence
                + CLIENT_ID_SEQUENCE_SEPARATOR + randomSequence;

        try(final MockedStatic<OAuth2ClientCredentialsGeneratorAdapter> utilities = mockStatic(OAuth2ClientCredentialsGeneratorAdapter.class, CALLS_REAL_METHODS)) {
            utilities.when(() -> generateRandomString(CLIENT_ID_SEQUENCE_LENGTH)).thenReturn(randomSequence); // déjà testé

            assertEquals(expected, this.subject.generateClientId());

            utilities.verify(
                    () -> generateRandomString(CLIENT_ID_SEQUENCE_LENGTH),
                    times(CLIENT_ID_SEQUENCE_COUNT)
            );
        }
    }
}