package io.github.tbondetti.authserver.core.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OAuth2CreatedClient( // record à n'utiliser qu'au moment de la création
        UUID id,
        String clientId,
        String clientName,
        String clientSecret,
        String applicationCode
) {
}