package io.github.tbondetti.authserver.web.dto;

public record CreateUserRequest(
        String username,
        String password
) { }

