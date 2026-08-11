package io.github.tbondetti.authserver.openapi.administration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.APPLICATION_CODE;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.CLIENT_NAME;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.GRANT_TYPES;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.REDIRECT_URIS;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.APPLICATION_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.APPLICATION_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_GRANT_TYPE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_GRANT_TYPE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_NAME_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_NAME_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_REDIRECT_URIS_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_REDIRECT_URIS_EXAMPLE;

public interface CreateOAuth2ClientRequestApi {

    @Schema(
            description = CLIENT_NAME_DESCRIPTION,
            example = CLIENT_NAME_EXAMPLE,
            maxLength = CLIENT_NAME_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(CLIENT_NAME)
    String clientName();

    @Schema(
            description = APPLICATION_CODE_DESCRIPTION,
            example = APPLICATION_CODE_EXAMPLE,
            maxLength = APPLICATION_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(APPLICATION_CODE)
    String applicationCode();

    @Schema(
            description = CLIENT_GRANT_TYPE_DESCRIPTION,
            example = CLIENT_GRANT_TYPE_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(GRANT_TYPES)
    Collection<String> grantTypes();

    @Schema(
            description = CLIENT_REDIRECT_URIS_DESCRIPTION,
            example = CLIENT_REDIRECT_URIS_EXAMPLE
    )
    @JsonProperty(REDIRECT_URIS)
    Collection<String> redirectUris();


}
