package io.github.tbondetti.authserver.web.dto;

public record CreateOAuth2ClientRequest(
        String clientName,
        String applicationCode
) { }

