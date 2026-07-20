package io.github.tbondetti.authserver.infrastructure.web.dto;

public record CreateRoleRequest(
        String code,
        String name,
        String description,
        String codeApplication
) { }
