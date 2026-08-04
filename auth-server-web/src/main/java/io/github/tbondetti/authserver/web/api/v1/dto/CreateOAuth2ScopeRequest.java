package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.CreateOAuth2ScopeRequestApi;

public record CreateOAuth2ScopeRequest(
        String applicationCode,
        String code,
        String name,
        String description
) implements CreateOAuth2ScopeRequestApi { }

