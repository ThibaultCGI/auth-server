package io.github.tbondetti.authserver.openapi.administration.api;

import io.github.tbondetti.authserver.openapi.administration.dto.CreateApplicationRequestApi;
import io.github.tbondetti.authserver.openapi.administration.response.ApplicationResponseApi;
import io.github.tbondetti.authserver.openapi.common.response.ApiErrorResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.CODE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.CODE_PARAMETER_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.CREATE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.CREATE_REQUEST_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.CREATE_SUMMARY;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.GET_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.GET_SUMMARY;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.RESPONSE_200_OK;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.RESPONSE_201_CREATED;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.TAG;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.TAG_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_400_BAD_REQUEST;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_401_UNAUTHORIZED;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_403_FORBIDDEN;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.RESPONSE_500_INTERNAL_SERVER_ERROR;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.SECURITY_SCHEME_NAME;

@Tag(
        name = TAG,
        description = TAG_DESCRIPTION
)
public interface ApplicationApi<
        C extends CreateApplicationRequestApi
> {

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
                            implementation = ApplicationResponseApi.class
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
    ApplicationResponseApi getApplication(
            @Parameter(
                    description = CODE_PARAMETER_DESCRIPTION,
                    required = true,
                    example = CODE_EXAMPLE
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
                            implementation = ApplicationResponseApi.class
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
    ApplicationResponseApi createApplication(
            @RequestBody(
                    description = CREATE_REQUEST_DESCRIPTION,
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = CreateApplicationRequestApi.class
                            )
                    )
            )
            final C request
    );
}