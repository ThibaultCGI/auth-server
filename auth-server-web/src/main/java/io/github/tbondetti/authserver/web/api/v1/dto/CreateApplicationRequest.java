package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.web.openapi.dto.CreateApplicationRequestApi;

public record CreateApplicationRequest(
        String code,
        String name,
        String description
) implements CreateApplicationRequestApi { }
