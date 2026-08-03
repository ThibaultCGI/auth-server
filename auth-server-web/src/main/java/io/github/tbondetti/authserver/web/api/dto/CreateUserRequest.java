package io.github.tbondetti.authserver.web.api.dto;

public record CreateUserRequest(
        String username,
        String password
) { }

