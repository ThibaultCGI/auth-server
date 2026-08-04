package io.github.tbondetti.authserver.web.api.v1.error;

import io.github.tbondetti.authserver.core.exception.AuthServerErrorCode;
import io.github.tbondetti.authserver.web.openapi.response.ApiErrorResponseApi;
import lombok.Builder;

@Builder
public record ApiErrorResponse (
        AuthServerErrorCode code,
        String description
) implements ApiErrorResponseApi { }