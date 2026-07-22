package io.github.tbondetti.authserver.infrastructure.web.dto;

public record CreateApplicationRequest(
        String code,
        String name,
        String description
) { }
