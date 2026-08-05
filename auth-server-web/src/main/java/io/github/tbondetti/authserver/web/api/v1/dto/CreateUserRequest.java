package io.github.tbondetti.authserver.web.api.v1.dto;

public record CreateUserRequest(
        String username,
        String password
) { }

