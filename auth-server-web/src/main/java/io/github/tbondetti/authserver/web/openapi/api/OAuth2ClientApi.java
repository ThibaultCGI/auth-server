package io.github.tbondetti.authserver.web.openapi.api;

import io.github.tbondetti.authserver.web.api.v1.dto.CreateOAuth2ClientRequest;
import io.github.tbondetti.authserver.web.api.v1.error.ApiErrorResponse;
import io.github.tbondetti.authserver.web.api.v1.response.CreateOAuth2ClientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CREATE_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CREATE_REQUEST_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.CREATE_SUMMARY;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.RESPONSE_201_CREATED;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.TAG;
import static io.github.tbondetti.authserver.web.openapi.constants.OAuth2ClientOpenApiConstants.TAG_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_400_BAD_REQUEST;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_401_UNAUTHORIZED;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_403_FORBIDDEN;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.RESPONSE_500_INTERNAL_SERVER_ERROR;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.SECURITY_SCHEME_NAME;

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
                            implementation = ApiErrorResponse.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = RESPONSE_401_UNAUTHORIZED,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponse.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "403",
            description = RESPONSE_403_FORBIDDEN,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponse.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = RESPONSE_500_INTERNAL_SERVER_ERROR,
            content = @Content(
                    schema = @Schema(
                            implementation = ApiErrorResponse.class
                    )
            )
    )
    CreateOAuth2ClientResponse createOAuth2Client(@RequestBody(
            description = CREATE_REQUEST_DESCRIPTION,
            required = true,
            content = @Content(
                    schema = @Schema(
                            implementation = CreateOAuth2ClientRequest.class
                    )
            )
    ) final CreateOAuth2ClientRequest request);
}
