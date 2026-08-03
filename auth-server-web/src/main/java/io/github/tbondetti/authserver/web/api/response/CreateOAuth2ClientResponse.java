package io.github.tbondetti.authserver.web.api.response;

import lombok.Builder;

@Builder
public record CreateOAuth2ClientResponse(
        String clientId,
        String clientName,
        String clientSecret,
        String applicationCode
) { }
