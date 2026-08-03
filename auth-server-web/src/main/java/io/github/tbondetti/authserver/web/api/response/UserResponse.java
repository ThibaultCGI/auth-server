package io.github.tbondetti.authserver.web.api.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        boolean enabled
) { }
