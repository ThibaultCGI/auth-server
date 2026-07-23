package io.github.tbondetti.authserver.infrastructure.web.dto;

public record CreateOAuth2ClientRequest(
        String clientName,
        String applicationCode
) { }

