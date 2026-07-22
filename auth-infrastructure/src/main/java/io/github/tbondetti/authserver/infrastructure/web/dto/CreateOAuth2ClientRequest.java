package io.github.tbondetti.authserver.infrastructure.web.dto;

public record CreateOAuth2ClientRequest(
        String clientId,
        String clientName,
        String clientSecret,
        String applicationCode
) { }

