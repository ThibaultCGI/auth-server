package io.github.tbondetti.authserver.openapi.authorizationserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.ALG;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.E;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.KID;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.KTY;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.N;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.USE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_ALG_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_ALG_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_E_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_E_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_KID_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_KID_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_KTY_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_KTY_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_N_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_N_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_USE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWK_USE_EXAMPLE;

public interface JwkApi {

    @Schema(
            description = JWK_KTY_DESCRIPTION,
            example = JWK_KTY_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(KTY)
    String kty();

    @Schema(
            description = JWK_USE_DESCRIPTION,
            example = JWK_USE_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(USE)
    String use();

    @Schema(
            description = JWK_ALG_DESCRIPTION,
            example = JWK_ALG_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(ALG)
    String alg();

    @Schema(
            description = JWK_KID_DESCRIPTION,
            example = JWK_KID_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(KID)
    String kid();

    @Schema(
            description = JWK_N_DESCRIPTION,
            example = JWK_N_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(N)
    String n();

    @Schema(
            description = JWK_E_DESCRIPTION,
            example = JWK_E_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(E)
    String e();
}
