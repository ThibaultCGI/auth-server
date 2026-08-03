package io.github.tbondetti.authserver.web.api.dto;

public record CreateApplicationRequest(
        String code,
        String name,
        String description
) { }
