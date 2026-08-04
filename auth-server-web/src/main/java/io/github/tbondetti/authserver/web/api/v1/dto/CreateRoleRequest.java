package io.github.tbondetti.authserver.web.api.v1.dto;

public record CreateRoleRequest(
        String codeApplication,
        String code,
        String name,
        String description
) { }
