package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.CreateOAuth2ScopeRequestApi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_PATTERN;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_APPLICATION_CODE_IS_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_CODE_HAS_INVALID_CARACTER;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_CODE_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_CODE_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_DESCRIPTION_TOO_LONG;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_NAME_IS_REQUIRED;
import static io.github.tbondetti.authserver.core.constants.ValidationErrorMessages.ERROR_SCOPE_NAME_TOO_LONG;

public record CreateOAuth2ScopeRequest(

        @NotBlank(
                message = ERROR_APPLICATION_CODE_IS_REQUIRED
        )
        @Size(
                max = APPLICATION_CODE_MAX_LENGTH,
                message = ERROR_APPLICATION_CODE_IS_TOO_LONG
        )
        String applicationCode,

        @NotBlank(
                message = ERROR_SCOPE_CODE_IS_REQUIRED
        )
        @Size(
                max = SCOPE_CODE_MAX_LENGTH,
                message = ERROR_SCOPE_CODE_TOO_LONG
        )
        @Pattern(
                regexp = SCOPE_CODE_PATTERN,
                message = ERROR_SCOPE_CODE_HAS_INVALID_CARACTER
        )
        String code,

        @NotBlank(
                message = ERROR_SCOPE_NAME_IS_REQUIRED
        )
        @Size(
                max = SCOPE_NAME_MAX_LENGTH,
                message = ERROR_SCOPE_NAME_TOO_LONG
        )
        String name,

        @Size(
                max = SCOPE_DESCRIPTION_MAX_LENGTH,
                message = ERROR_SCOPE_DESCRIPTION_TOO_LONG
        )
        String description

) implements CreateOAuth2ScopeRequestApi { }
