package io.github.tbondetti.authserver.web.openapi.response;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_ID_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_SECRET_MAX_LENGTH;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.APPLICATION_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.APPLICATION_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CLIENT_ID_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CLIENT_ID_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CLIENT_NAME_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CLIENT_NAME_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CLIENT_SECRET_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CLIENT_SECRET_EXAMPLE;

public interface CreateOAuth2ClientResponseApi {

    @Schema(
            description = CLIENT_ID_DESCRIPTION,
            example = CLIENT_ID_EXAMPLE,
            minLength = CLIENT_ID_LENGTH,
            maxLength = CLIENT_ID_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String clientId();

    @Schema(
            description = CLIENT_NAME_DESCRIPTION,
            example = CLIENT_NAME_EXAMPLE,
            maxLength = CLIENT_NAME_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String clientName();

    @Schema(
            description = CLIENT_SECRET_DESCRIPTION,
            example = CLIENT_SECRET_EXAMPLE,
            minLength = CLIENT_SECRET_MAX_LENGTH,
            maxLength = CLIENT_SECRET_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String clientSecret();

    @Schema(
            description = APPLICATION_CODE_DESCRIPTION,
            example = APPLICATION_CODE_EXAMPLE,
            maxLength = APPLICATION_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String applicationCode();
}
