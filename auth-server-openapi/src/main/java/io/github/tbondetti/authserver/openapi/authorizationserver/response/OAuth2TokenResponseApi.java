package io.github.tbondetti.authserver.openapi.authorizationserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.ACCESS_TOKEN;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.EXPIRES_IN;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.SCOPE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.TOKEN_TYPE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.ACCESS_TOKEN_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.ACCESS_TOKEN_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.EXPIRES_IN_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.EXPIRES_IN_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.GRANTED_SCOPE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.GRANTED_SCOPE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.TOKEN_TYPE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.TOKEN_TYPE_EXAMPLE;

public interface OAuth2TokenResponseApi {

    @Schema(
            description = ACCESS_TOKEN_DESCRIPTION,
            example = ACCESS_TOKEN_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(ACCESS_TOKEN)
    String accessToken();

    @Schema(
            description = TOKEN_TYPE_DESCRIPTION,
            example = TOKEN_TYPE_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(TOKEN_TYPE)
    String tokenType();

    @Schema(
            description = EXPIRES_IN_DESCRIPTION,
            example = EXPIRES_IN_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(EXPIRES_IN)
    Long expiresIn();

    @Schema(
            description = GRANTED_SCOPE_DESCRIPTION,
            example = GRANTED_SCOPE_EXAMPLE
    )
    @JsonProperty(SCOPE)
    String scope();
}
