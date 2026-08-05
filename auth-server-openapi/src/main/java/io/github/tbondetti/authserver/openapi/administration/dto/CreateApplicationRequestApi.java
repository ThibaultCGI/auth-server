package io.github.tbondetti.authserver.openapi.administration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.CODE;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.AdministrationClaimsNames.NAME;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.CODE_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.CODE_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.DESCRIPTION_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.DESCRIPTION_EXAMPLE;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.NAME_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.administration.constants.ApplicationOpenApiConstants.NAME_EXAMPLE;

public interface CreateApplicationRequestApi {

    @Schema(
            description = CODE_DESCRIPTION,
            example = CODE_EXAMPLE,
            maxLength = APPLICATION_CODE_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(CODE)
    String code();

    @Schema(
            description = NAME_DESCRIPTION,
            example = NAME_EXAMPLE,
            maxLength = APPLICATION_NAME_MAX_LENGTH,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(NAME)
    String name();

    @Schema(
            description = DESCRIPTION_DESCRIPTION,
            example = DESCRIPTION_EXAMPLE,
            maxLength = APPLICATION_DESCRIPTION_MAX_LENGTH
    )
    @JsonProperty(DESCRIPTION)
    String description();
}