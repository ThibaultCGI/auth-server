package io.github.tbondetti.authserver.web.api.v1.dto;

public record AssignRoleRequest(
        String applicationCode,
        String roleCode
) { }