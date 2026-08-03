package io.github.tbondetti.authserver.infrastructure.web.dto;

public record AssignRoleRequest(
        String applicationCode,
        String roleCode
) { }