package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RoleResponse(
        UUID id,
        String codeApplication,
        String code,
        String name,
        String description
) { }
