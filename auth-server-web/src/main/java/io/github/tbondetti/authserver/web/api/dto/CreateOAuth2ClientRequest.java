package io.github.tbondetti.authserver.web.api.dto;

public record CreateOAuth2ClientRequest(
        String clientName,
        String applicationCode
) { }

