package io.github.tbondetti.authserver.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "jwt.keystore")
public record JwtKeyStoreProperties(
        String location,
        String storePassword,
        Map<String, String> keys,
        String activeAlias
) {
    public boolean isActive(final String alias) {
        return activeAlias.equals(alias);
    }
}
