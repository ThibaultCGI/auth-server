package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.CreateOAuth2ClientRequestApi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Collection;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_GRANT_TYPE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_CLIENT_NAME_IS_TOO_LONG;

public record CreateOAuth2ClientRequest(

        @NotBlank(
                message = ERROR_CLIENT_NAME_IS_REQUIRED
        )
        @Size(
                max = CLIENT_NAME_MAX_LENGTH,
                message = ERROR_CLIENT_NAME_IS_TOO_LONG
        )
        String clientName,

        @NotBlank(
                message = ERROR_APPLICATION_CODE_IS_REQUIRED
        )
        @Size(
                max = APPLICATION_CODE_MAX_LENGTH,
                message = ERROR_APPLICATION_CODE_IS_TOO_LONG
        )
        String applicationCode,

        @NotEmpty(
                message = ERROR_CLIENT_GRANT_TYPE_IS_REQUIRED
        )
        Collection<String> grantTypes,

        Collection<String> redirectUris
) implements CreateOAuth2ClientRequestApi { }
