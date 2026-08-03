package io.github.tbondetti.authserver.web.api.error;

import io.github.tbondetti.authserver.core.exception.AuthServerErrorCode;
import lombok.Builder;

@Builder
public record ApiErrorResponse (
        AuthServerErrorCode code,
        String description
) { }