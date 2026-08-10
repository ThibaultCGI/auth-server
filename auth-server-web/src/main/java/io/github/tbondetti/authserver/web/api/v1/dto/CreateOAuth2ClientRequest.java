package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.CreateOAuth2ClientRequestApi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;

public record CreateOAuth2ClientRequest(

        @NotBlank
        @Size(max = CLIENT_NAME_MAX_LENGTH)
        String clientName,

        @NotBlank
        @Size(max = APPLICATION_CODE_MAX_LENGTH)
        String applicationCode

) implements CreateOAuth2ClientRequestApi { }
