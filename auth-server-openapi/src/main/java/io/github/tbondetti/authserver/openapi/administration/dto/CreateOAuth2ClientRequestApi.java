package io.github.tbondetti.authserver.openapi.administration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.APPLICATION_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.APPLICATION_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_NAME_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_NAME_EXAMPLE;

public interface CreateOAuth2ClientRequestApi {

    @Schema(
            description = CLIENT_NAME_DESCRIPTION,
            example = CLIENT_NAME_EXAMPLE,
            maxLength = CLIENT_NAME_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String clientName();

    @Schema(
            description = APPLICATION_CODE_DESCRIPTION,
            example = APPLICATION_CODE_EXAMPLE,
            maxLength = APPLICATION_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String applicationCode();


}
