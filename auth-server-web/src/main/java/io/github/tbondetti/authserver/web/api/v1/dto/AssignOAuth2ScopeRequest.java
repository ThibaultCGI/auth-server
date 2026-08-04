package io.github.tbondetti.authserver.web.api.v1.dto;

public record AssignOAuth2ScopeRequest(
        String applicationCode,
        String code
) { }

