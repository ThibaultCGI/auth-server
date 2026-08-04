package io.github.tbondetti.authserver.web.api.v1.response;

import io.github.tbondetti.authserver.web.openapi.response.ApplicationResponseApi;
import lombok.Builder;

@Builder
public record ApplicationResponse(
        String code,
        String name,
        String description
) implements ApplicationResponseApi { }
