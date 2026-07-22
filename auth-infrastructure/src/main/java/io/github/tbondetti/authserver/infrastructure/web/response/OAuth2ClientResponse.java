package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OAuth2ClientResponse(
        UUID id,
        String clientId,
        String clientName,
        String applicationCode
) { }
