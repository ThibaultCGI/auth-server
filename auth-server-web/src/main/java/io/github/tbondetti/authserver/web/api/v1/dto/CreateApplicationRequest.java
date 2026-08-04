package io.github.tbondetti.authserver.web.api.v1.dto;

public record CreateApplicationRequest(
        String code,
        String name,
        String description
) { }
