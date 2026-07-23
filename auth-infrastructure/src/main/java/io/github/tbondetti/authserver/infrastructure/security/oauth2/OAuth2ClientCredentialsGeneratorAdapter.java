package io.github.tbondetti.authserver.infrastructure.security.oauth2;

import io.github.tbondetti.authserver.core.port.OAuth2ClientCredentialsGeneratorPort;

import java.security.SecureRandom;

public class OAuth2ClientCredentialsGeneratorAdapter implements OAuth2ClientCredentialsGeneratorPort {

    static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    static final int CLIENT_ID_SEQUENCE_COUNT = 4;
    static final int CLIENT_ID_SEQUENCE_LENGTH = 6;
    static final String CLIENT_ID_SEQUENCE_SEPARATOR = "-";
    static final int CLIENT_SECRET_LENGTH = 50;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String generateClientId() {
        final StringBuilder result = new StringBuilder(generatedClientIdLength());

        for (int i = 0; i < CLIENT_ID_SEQUENCE_COUNT; i++) {
            result.append(generateRandomString(CLIENT_ID_SEQUENCE_LENGTH));
            if (i < CLIENT_ID_SEQUENCE_COUNT - 1) {
                result.append(CLIENT_ID_SEQUENCE_SEPARATOR);
            }
        }

        return result.toString();
    }

    @Override
    public String generateClientSecret() {
        return generateRandomString(CLIENT_SECRET_LENGTH);
    }

    static int generatedClientIdLength() {
        return CLIENT_ID_SEQUENCE_LENGTH * CLIENT_ID_SEQUENCE_COUNT + (CLIENT_ID_SEQUENCE_COUNT - 1);
    }

    static String generateRandomString(final int sequenceLength) {
        final StringBuilder result = new StringBuilder(sequenceLength);

        for (int i = 0; i < sequenceLength; i++) {
            result.append(ALPHABET.charAt(SECURE_RANDOM.nextInt(ALPHABET.length())));
        }

        return result.toString();
    }
}
