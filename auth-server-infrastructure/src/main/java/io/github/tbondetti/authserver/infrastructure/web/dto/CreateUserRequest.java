package io.github.tbondetti.authserver.infrastructure.web.dto;

public record CreateUserRequest(
        String username,
        String password
) { }

