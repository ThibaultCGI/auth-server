package io.github.tbondetti.authserver.infrastructure.web.dto;

public record CreateRoleRequest(
        String codeApplication,
        String code,
        String name,
        String description
) { }
