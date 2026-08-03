package io.github.tbondetti.authserver.web.api.response;

import lombok.Builder;

@Builder
public record RoleResponse(
        String codeApplication,
        String code,
        String name,
        String description
) { }
