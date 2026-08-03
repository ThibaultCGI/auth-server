package io.github.tbondetti.authserver.core.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Role (
        UUID id,
        String codeApplication,
        String code,
        String name,
        String description
) { }
