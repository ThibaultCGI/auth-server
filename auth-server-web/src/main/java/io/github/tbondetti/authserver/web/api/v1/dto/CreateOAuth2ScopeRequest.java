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

public record CreateOAuth2ScopeRequest(

        @NotBlank
        @Size(max = APPLICATION_CODE_MAX_LENGTH)
        String applicationCode,

        @NotBlank
        @Size(max = SCOPE_CODE_MAX_LENGTH)
        @Pattern(regexp = SCOPE_CODE_PATTERN)
        String code,

        @NotBlank
        @Size(max = SCOPE_NAME_MAX_LENGTH)
        String name,

        @Size(max = SCOPE_DESCRIPTION_MAX_LENGTH)
        String description

) implements CreateOAuth2ScopeRequestApi { }
