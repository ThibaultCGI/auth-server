package io.github.tbondetti.authserver.web.api.v1.response;

import io.github.tbondetti.authserver.openapi.administration.response.CreateOAuth2ClientResponseApi;
import lombok.Builder;

import java.util.Set;

@Builder
public record CreateOAuth2ClientResponse(
        String clientId,
        String clientName,
        String clientSecret,
        String applicationCode,
        Set<String> grantTypes,
        Set<String> redirectUris
) implements CreateOAuth2ClientResponseApi { }
