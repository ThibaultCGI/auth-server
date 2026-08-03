package io.github.tbondetti.authserver.web.response;

import lombok.Builder;

@Builder
public record CreateOAuth2ClientResponse(
        String clientId,
        String clientName,
        String clientSecret,
        String applicationCode
) { }
