package io.github.tbondetti.authserver.web.openapi.response;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.APPLICATION_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.APPLICATION_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_DESCRIPTION_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_DESCRIPTION_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_NAME_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_NAME_EXAMPLE;

public interface OAuth2ScopeResponseApi {

    @Schema(
            description = APPLICATION_CODE_DESCRIPTION,
            example = APPLICATION_CODE_EXAMPLE,
            maxLength = APPLICATION_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String applicationCode();

    @Schema(
            description = SCOPE_CODE_DESCRIPTION,
            example = SCOPE_CODE_EXAMPLE,
            maxLength = SCOPE_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String code();

    @Schema(
            description = SCOPE_NAME_DESCRIPTION,
            example = SCOPE_NAME_EXAMPLE,
            maxLength = SCOPE_NAME_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String name();

    @Schema(
            description = SCOPE_DESCRIPTION_DESCRIPTION,
            example = SCOPE_DESCRIPTION_EXAMPLE,
            maxLength = SCOPE_DESCRIPTION_MAX_LENGTH
    )
    String description();
}