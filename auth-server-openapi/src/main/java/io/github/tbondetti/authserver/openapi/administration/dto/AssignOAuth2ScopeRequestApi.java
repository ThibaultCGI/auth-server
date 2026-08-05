package io.github.tbondetti.authserver.openapi.administration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.APPLICATION_CODE;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.CODE;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ScopeOpenApiConstants.APPLICATION_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ScopeOpenApiConstants.APPLICATION_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ScopeOpenApiConstants.SCOPE_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ScopeOpenApiConstants.SCOPE_CODE_EXAMPLE;

public interface AssignOAuth2ScopeRequestApi {

    @Schema(
            description = APPLICATION_CODE_DESCRIPTION,
            example = APPLICATION_CODE_EXAMPLE,
            maxLength = APPLICATION_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(APPLICATION_CODE)
    String applicationCode();

    @Schema(
            description = SCOPE_CODE_DESCRIPTION,
            example = SCOPE_CODE_EXAMPLE,
            maxLength = SCOPE_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(CODE)
    String code();
}