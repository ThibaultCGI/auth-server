package io.github.tbondetti.authserver.web.api.v1.dto;

public record CreateOAuth2ClientRequest(
        String clientName,
        String applicationCode
) { }

