package io.github.tbondetti.authserver.web.api.dto;

public record AssignRoleRequest(
        String applicationCode,
        String roleCode
) { }