package io.github.tbondetti.authserver.web.api.v1.response;

import io.github.tbondetti.authserver.openapi.administration.response.OAuth2ClientResponseApi;
import lombok.Builder;

@Builder
public record OAuth2ClientResponse(
        String clientId,
        String clientName,
        String applicationCode
) implements OAuth2ClientResponseApi { }
