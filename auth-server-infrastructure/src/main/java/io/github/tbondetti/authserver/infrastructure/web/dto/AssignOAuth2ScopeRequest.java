package io.github.tbondetti.authserver.infrastructure.web.dto;

public record AssignOAuth2ScopeRequest(
        String applicationCode,
        String code
) { }

