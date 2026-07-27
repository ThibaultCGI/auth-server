package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

@Builder
public record OAuth2ScopeResponse(
        String applicationCode,
        String code,
        String name,
        String description
) { }
