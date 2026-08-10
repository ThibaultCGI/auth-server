package io.github.tbondetti.authserver.core.domain;

import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import lombok.Builder;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

@Builder
public record OAuth2Client(
        UUID id,
        String clientId,
        String clientName,
        String clientSecretHash,
        String applicationCode,
        Set<URI> redirectUris,
        Set<OAuth2ClientGrantType> grantTypes
) { }
