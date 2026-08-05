package io.github.tbondetti.authserver.openapi.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.GRANT_TYPE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.SCOPE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.GRANT_TYPE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.GRANT_TYPE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.SCOPE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.SCOPE_EXAMPLE;

public interface OAuth2TokenRequestApi {

    @Schema(
            description = GRANT_TYPE_DESCRIPTION,
            example = GRANT_TYPE_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(GRANT_TYPE)
    String grantType();

    @Schema(
            description = SCOPE_DESCRIPTION,
            example = SCOPE_EXAMPLE
    )
    @JsonProperty(SCOPE)
    String scope();
}
