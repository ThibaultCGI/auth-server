package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

@Builder
public record ApplicationResponse(
        String code,
        String name,
        String description
) { }
