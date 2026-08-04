package io.github.tbondetti.authserver.web.openapi.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.API_VERSION;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.OAUTH2_ADMINISTRATION_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.OAUTH2_ADMINISTRATION_DISPLAY_NAME;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.OAUTH2_ADMINISTRATION_GROUP;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.OAUTH2_ADMINISTRATION_PATH_APPLICATIONS;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.OAUTH2_ADMINISTRATION_PATH_CLIENTS;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.OAUTH2_ADMINISTRATION_PATH_SCOPES;

@Configuration
public class OAuth2AdministrationOpenApiConfiguration {

    private Info info() {
        return new Info()
                .title(OAUTH2_ADMINISTRATION_DISPLAY_NAME)
                .description(OAUTH2_ADMINISTRATION_DESCRIPTION)
                .version(API_VERSION)
                ;
    }

    @Bean
    public GroupedOpenApi oauth2AdministrationApi() {
        return GroupedOpenApi.builder()
                .group(OAUTH2_ADMINISTRATION_GROUP)
                .displayName(OAUTH2_ADMINISTRATION_DISPLAY_NAME)
                .pathsToMatch(
                        OAUTH2_ADMINISTRATION_PATH_APPLICATIONS,
                        OAUTH2_ADMINISTRATION_PATH_CLIENTS,
                        OAUTH2_ADMINISTRATION_PATH_SCOPES
                )
                .addOpenApiCustomizer(openApi -> openApi.info(this.info()))
                .build();
    }
}
