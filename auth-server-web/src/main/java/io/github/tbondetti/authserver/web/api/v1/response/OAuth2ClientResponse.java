package io.github.tbondetti.authserver.web.api.v1.response;

import io.github.tbondetti.authserver.openapi.administration.response.OAuth2ClientResponseApi;
import lombok.Builder;

import java.util.Set;

@Builder
public record OAuth2ClientResponse(
        String clientId,
        String clientName,
        String applicationCode,
        Set<String> grantTypes,
        Set<String> redirectUris
) implements OAuth2ClientResponseApi { }
