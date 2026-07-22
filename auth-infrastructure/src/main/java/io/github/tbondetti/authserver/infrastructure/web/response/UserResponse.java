package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        boolean enabled
) { }
