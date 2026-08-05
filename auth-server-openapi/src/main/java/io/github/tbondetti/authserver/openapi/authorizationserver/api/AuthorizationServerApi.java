package io.github.tbondetti.authserver.openapi.authorizationserver.api;

import io.github.tbondetti.authserver.openapi.authorizationserver.dto.OAuth2TokenRequestApi;
import io.github.tbondetti.authserver.openapi.authorizationserver.response.JwksResponseApi;
import io.github.tbondetti.authserver.openapi.authorizationserver.response.OAuth2TokenResponseApi;
import io.github.tbondetti.authserver.openapi.common.response.ApiErrorResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.*;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.*;

@Tag(
        name = TAG,
        description = TAG_DESCRIPTION
)
public interface AuthorizationServerApi {

    @Operation(
            summary = TOKEN_SUMMARY,
            description = TOKEN_DESCRIPTION
    )
    @SecurityRequirement(
            name = SECURITY_SCHEME_NAME
    )
    @ApiResponse(
            responseCode = "200",
            description = TOKEN_RESPONSE_200,
            content = @Content(
                    schema = @Schema(
                            implementation = OAuth2TokenResponseApi.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = RESPONSE_400_BAD_REQUEST,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponseApi.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = RESPONSE_401_UNAUTHORIZED,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponseApi.class
                    )
            )
    )
    OAuth2TokenResponseApi token(
            @RequestBody(
                    description = TOKEN_REQUEST_DESCRIPTION,
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = OAuth2TokenRequestApi.class
                            )
                    )
            )
            final OAuth2TokenRequestApi request
    );

    @Operation(
            summary = JWKS_SUMMARY,
            description = JWKS_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "200",
            description = JWKS_RESPONSE_200,
            content = @Content(
                    schema = @Schema(
                            implementation = JwksResponseApi.class
                    )
            )
    )
    JwksResponseApi jwks();
}