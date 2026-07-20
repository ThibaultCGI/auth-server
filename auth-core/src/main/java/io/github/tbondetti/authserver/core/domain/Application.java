package io.github.tbondetti.authserver.core.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Application(
        UUID id,
        String code,
        String name,
        String description
) { }
