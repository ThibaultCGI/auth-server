package io.github.tbondetti.authserver.web.api.response;

import lombok.Builder;

@Builder
public record OAuth2ScopeResponse(
        String applicationCode,
        String code,
        String name,
        String description
) { }
