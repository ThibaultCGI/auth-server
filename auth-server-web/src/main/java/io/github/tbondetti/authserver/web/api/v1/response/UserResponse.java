package io.github.tbondetti.authserver.web.api.v1.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        boolean enabled
) { }
