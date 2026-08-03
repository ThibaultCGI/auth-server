package io.github.tbondetti.authserver.core.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OAuth2Client(
        UUID id,
        String clientId,
        String clientName,
        String clientSecretHash,
        String applicationCode
) {
}