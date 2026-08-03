package io.github.tbondetti.authserver.web.dto;

public record CreateApplicationRequest(
        String code,
        String name,
        String description
) { }
