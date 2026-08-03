package io.github.tbondetti.authserver.web.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        boolean enabled
) { }
