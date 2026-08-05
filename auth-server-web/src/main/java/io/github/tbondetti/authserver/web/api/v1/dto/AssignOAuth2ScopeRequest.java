package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.AssignOAuth2ScopeRequestApi;

public record AssignOAuth2ScopeRequest(
        String applicationCode,
        String code
) implements AssignOAuth2ScopeRequestApi { }

