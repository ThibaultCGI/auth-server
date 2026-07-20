package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ApplicationResponse(
        UUID id,
        String code,
        String name,
        String description
) { }
