package io.github.tbondetti.authserver.web.dto;

public record AssignOAuth2ScopeRequest(
        String applicationCode,
        String code
) { }

