package io.github.tbondetti.authserver.openapi.administration.response;

import io.github.tbondetti.authserver.core.exception.AuthServerErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.API_ERROR_RESPONSE_CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.API_ERROR_RESPONSE_CODE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.API_ERROR_RESPONSE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.API_ERROR_RESPONSE_DESCRIPTION_DESCRIPTION;

@Schema(
        description = API_ERROR_RESPONSE_DESCRIPTION
)
public interface ApiErrorResponseApi {

    @Schema(
            description = API_ERROR_RESPONSE_CODE_DESCRIPTION,
            example = API_ERROR_RESPONSE_CODE_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    AuthServerErrorCode code();

    @Schema(
            description = API_ERROR_RESPONSE_DESCRIPTION_DESCRIPTION,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String description();
}
