package io.github.tbondetti.authserver.web.api.v1.response;

import lombok.Builder;

@Builder
public record OAuth2ScopeResponse(
        String applicationCode,
        String code,
        String name,
        String description
) { }
