package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.CreateApplicationRequestApi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_DESCRIPTION_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_NAME_IS_TOO_LONG;

public record CreateApplicationRequest(
        @NotBlank(
                message = ERROR_APPLICATION_CODE_IS_REQUIRED
        )
        @Size(
                message = ERROR_APPLICATION_CODE_IS_TOO_LONG,
                max = APPLICATION_CODE_MAX_LENGTH
        )
        String code,

        @NotBlank(
                message = ERROR_APPLICATION_NAME_IS_REQUIRED
        )
        @Size(
                max = APPLICATION_NAME_MAX_LENGTH,
                message = ERROR_APPLICATION_NAME_IS_TOO_LONG
        )
        String name,

        @Size(
                max = APPLICATION_DESCRIPTION_MAX_LENGTH,
                message = ERROR_APPLICATION_DESCRIPTION_IS_TOO_LONG
        )
        String description
) implements CreateApplicationRequestApi { }
