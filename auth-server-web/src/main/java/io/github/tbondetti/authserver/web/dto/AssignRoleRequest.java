package io.github.tbondetti.authserver.web.dto;

public record AssignRoleRequest(
        String applicationCode,
        String roleCode
) { }