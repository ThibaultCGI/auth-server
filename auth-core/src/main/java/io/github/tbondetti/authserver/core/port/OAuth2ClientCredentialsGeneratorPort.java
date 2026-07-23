package io.github.tbondetti.authserver.core.port;

public interface OAuth2ClientCredentialsGeneratorPort {

    String generateClientId();

    String generateClientSecret();
}
