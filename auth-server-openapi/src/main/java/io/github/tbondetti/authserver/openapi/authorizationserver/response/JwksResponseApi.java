package io.github.tbondetti.authserver.openapi.authorizationserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerClaimsNames.KEYS;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWKS_KEYS_DESCRIPTION;

public interface JwksResponseApi {

    @ArraySchema(
            schema = @Schema(
                    implementation = JwkApi.class
            ),
            arraySchema = @Schema(
                    description = JWKS_KEYS_DESCRIPTION
            )
    )
    @JsonProperty(KEYS)
    List<JwkApi> keys();
}
