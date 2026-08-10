package io.github.tbondetti.authserver.web.api.v1.dto;

import io.github.tbondetti.authserver.openapi.administration.dto.AssignOAuth2ScopeRequestApi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_PATTERN;

public record AssignOAuth2ScopeRequest(

        @NotBlank
        @Size(max = APPLICATION_CODE_MAX_LENGTH)
        String applicationCode,

        @NotBlank
        @Size(max = SCOPE_CODE_MAX_LENGTH)
        @Pattern(regexp = SCOPE_CODE_PATTERN)
        String code

) implements AssignOAuth2ScopeRequestApi { }
