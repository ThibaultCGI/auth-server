package io.github.tbondetti.authserver.infrastructure.web.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        boolean enabled
) { }
