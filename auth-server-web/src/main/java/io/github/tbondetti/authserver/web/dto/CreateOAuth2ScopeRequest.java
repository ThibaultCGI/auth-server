package io.github.tbondetti.authserver.web.dto;

public record CreateOAuth2ScopeRequest(
        String applicationCode,
        String code,
        String name,
        String description
) { }

