package io.github.tbondetti.authserver.core.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record User (
        UUID id,
        String username,
        String passwordHash,
        boolean enabled
) { }
