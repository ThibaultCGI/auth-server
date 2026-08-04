package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.web.openapi.dto.CreateOAuth2ClientRequestApi;

public record CreateOAuth2ClientRequest(
        String clientName,
        String applicationCode
) implements CreateOAuth2ClientRequestApi { }

