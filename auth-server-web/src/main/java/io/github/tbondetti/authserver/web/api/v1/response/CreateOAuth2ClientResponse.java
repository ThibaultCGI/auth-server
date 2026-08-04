package io.github.tbondetti.authserver.web.api.v1.response;

import io.github.tbondetti.authserver.web.openapi.response.CreateOAuth2ClientResponseApi;
import lombok.Builder;

@Builder
public record CreateOAuth2ClientResponse(
        String clientId,
        String clientName,
        String clientSecret,
        String applicationCode
) implements CreateOAuth2ClientResponseApi { }
