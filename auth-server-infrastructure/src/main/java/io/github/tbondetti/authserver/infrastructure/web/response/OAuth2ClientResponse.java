package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

@Builder
public record OAuth2ClientResponse(
        String clientId,
        String clientName,
        String applicationCode
) { }
