package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

@Builder
public record RoleResponse(
        String codeApplication,
        String code,
        String name,
        String description
) { }
