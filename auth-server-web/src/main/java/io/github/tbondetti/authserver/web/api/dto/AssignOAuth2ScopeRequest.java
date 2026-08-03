package io.github.tbondetti.authserver.web.api.dto;

public record AssignOAuth2ScopeRequest(
        String applicationCode,
        String code
) { }

