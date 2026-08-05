package io.github.tbondetti.authserver.openapi.administration.api;

import io.github.tbondetti.authserver.openapi.administration.dto.AssignOAuth2ScopeRequestApi;
import io.github.tbondetti.authserver.openapi.administration.dto.CreateOAuth2ClientRequestApi;
import io.github.tbondetti.authserver.openapi.common.response.ApiErrorResponseApi;
import io.github.tbondetti.authserver.openapi.administration.response.CreateOAuth2ClientResponseApi;
import io.github.tbondetti.authserver.openapi.administration.response.OAuth2ClientResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.ASSIGN_SCOPE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.ASSIGN_SCOPE_REQUEST_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.ASSIGN_SCOPE_SUMMARY;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_ID_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CLIENT_ID_PARAMETER_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CREATE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CREATE_REQUEST_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.CREATE_SUMMARY;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.GET_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.GET_SUMMARY;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.RESPONSE_200_OK;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.RESPONSE_201_CREATED;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.RESPONSE_204_NO_CONTENT;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.TAG;
import static io.github.tbondetti.authserver.openapi.administration.constants.OAuth2ClientOpenApiConstants.TAG_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_400_BAD_REQUEST;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_401_UNAUTHORIZED;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_403_FORBIDDEN;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_500_INTERNAL_SERVER_ERROR;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.SECURITY_SCHEME_NAME;

@Tag(
        name = TAG,
        description = TAG_DESCRIPTION
)
public interface OAuth2ClientApi {

    @Operation(
            summary = CREATE_SUMMARY,
            description = CREATE_DESCRIPTION
    )
    @SecurityRequirement(
            name = SECURITY_SCHEME_NAME
    )
    @ApiResponse(
            responseCode = "201",
            description = RESPONSE_201_CREATED
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
    CreateOAuth2ClientResponseApi createOAuth2Client(@RequestBody(
            description = CREATE_REQUEST_DESCRIPTION,
            required = true,
            content = @Content(
                    schema = @Schema(
                            implementation = CreateOAuth2ClientRequestApi.class
                    )
            )
    ) final CreateOAuth2ClientRequestApi request);

    @Operation(
            summary = GET_SUMMARY,
            description = GET_DESCRIPTION
    )
    @SecurityRequirement(
            name = SECURITY_SCHEME_NAME
    )
    @ApiResponse(
            responseCode = "200",
            description = RESPONSE_200_OK
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
    OAuth2ClientResponseApi getOAuth2Client(
            @Parameter(
                    description = CLIENT_ID_PARAMETER_DESCRIPTION,
                    required = true,
                    example = CLIENT_ID_EXAMPLE
            )
            final String clientId
    );

    @Operation(
            summary = ASSIGN_SCOPE_SUMMARY,
            description = ASSIGN_SCOPE_DESCRIPTION
    )
    @SecurityRequirement(
            name = SECURITY_SCHEME_NAME
    )
    @ApiResponse(
            responseCode = "204",
            description = RESPONSE_204_NO_CONTENT
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
    void assignScope(
            @Parameter(
                    description = CLIENT_ID_PARAMETER_DESCRIPTION,
                    required = true,
                    example = CLIENT_ID_EXAMPLE
            )
            final String clientId,

            @RequestBody(
                    description = ASSIGN_SCOPE_REQUEST_DESCRIPTION,
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = AssignOAuth2ScopeRequestApi.class
                            )
                    )
            )
            final AssignOAuth2ScopeRequestApi request
    );

}
