package io.github.tbondetti.authserver.web.openapi.api;

import io.github.tbondetti.authserver.web.openapi.dto.CreateOAuth2ScopeRequestApi;
import io.github.tbondetti.authserver.web.openapi.response.ApiErrorResponseApi;
import io.github.tbondetti.authserver.web.openapi.response.OAuth2ScopeResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.APPLICATION_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.APPLICATION_CODE_PARAMETER_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.CREATE_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.CREATE_REQUEST_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.CREATE_SUMMARY;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.GET_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.GET_SUMMARY;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.RESPONSE_200_OK;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.RESPONSE_201_CREATED;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.SCOPE_CODE_PARAMETER_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.TAG;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ScopeOpenApiConstants.TAG_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_400_BAD_REQUEST;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_401_UNAUTHORIZED;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_403_FORBIDDEN;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_500_INTERNAL_SERVER_ERROR;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.SECURITY_SCHEME_NAME;

@Tag(
        name = TAG,
        description = TAG_DESCRIPTION
)
public interface OAuth2ScopeApi {

    @Operation(
            summary = GET_SUMMARY,
            description = GET_DESCRIPTION
    )
    @SecurityRequirement(
            name = SECURITY_SCHEME_NAME
    )
    @ApiResponse(
            responseCode = "200",
            description = RESPONSE_200_OK,
            content = @Content(
                    schema = @Schema(
                            implementation = OAuth2ScopeResponseApi.class
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
    @ApiResponse(
            responseCode = "403",
            description = RESPONSE_403_FORBIDDEN,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponseApi.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = RESPONSE_500_INTERNAL_SERVER_ERROR,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponseApi.class
                    )
            )
    )
    OAuth2ScopeResponseApi getOAuth2Scope(
            @Parameter(
                    description = APPLICATION_CODE_PARAMETER_DESCRIPTION,
                    required = true,
                    example = APPLICATION_CODE_EXAMPLE
            )
            final String applicationCode,

            @Parameter(
                    description = SCOPE_CODE_PARAMETER_DESCRIPTION,
                    required = true,
                    example = SCOPE_CODE_EXAMPLE
            )
            final String code
    );

    @Operation(
            summary = CREATE_SUMMARY,
            description = CREATE_DESCRIPTION
    )
    @SecurityRequirement(
            name = SECURITY_SCHEME_NAME
    )
    @ApiResponse(
            responseCode = "201",
            description = RESPONSE_201_CREATED,
            content = @Content(
                    schema = @Schema(
                            implementation = OAuth2ScopeResponseApi.class
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
    @ApiResponse(
            responseCode = "403",
            description = RESPONSE_403_FORBIDDEN,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponseApi.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = RESPONSE_500_INTERNAL_SERVER_ERROR,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponseApi.class
                    )
            )
    )
    OAuth2ScopeResponseApi createOAuth2Scope(
            @RequestBody(
                    description = CREATE_REQUEST_DESCRIPTION,
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = CreateOAuth2ScopeRequestApi.class
                            )
                    )
            )
            final CreateOAuth2ScopeRequestApi request
    );
}