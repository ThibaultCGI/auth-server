package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.CreateApplicationRequestApi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_NAME_MAX_LENGTH;

public record CreateApplicationRequest(
        @NotBlank
        @Size(
                message = " ",
                max = APPLICATION_CODE_MAX_LENGTH
        )
        String code,

        @NotBlank
        @Size(
                max = APPLICATION_NAME_MAX_LENGTH
        )
        String name,

        @Size(
                max = APPLICATION_DESCRIPTION_MAX_LENGTH
        )
        String description
) implements CreateApplicationRequestApi { }
