package io.github.tbondetti.authserver.openapi.authorizationserver.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.API_VERSION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_DISPLAY_NAME;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_GROUP;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_PATH_OAUTH2;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_PATH_WELL_KNOWN;

@Configuration
public class AuthorizationServerOpenApiConfiguration {

    private Info info() {
        return new Info()
                .title(AUTHORIZATION_SERVER_DISPLAY_NAME)
                .description(AUTHORIZATION_SERVER_DESCRIPTION)
                .version(API_VERSION)
                ;
    }

    @Bean
    public GroupedOpenApi authorizationServerApi() {
        return GroupedOpenApi.builder()
                .group(AUTHORIZATION_SERVER_GROUP)
                .displayName(AUTHORIZATION_SERVER_DISPLAY_NAME)
                .pathsToMatch(
                        AUTHORIZATION_SERVER_PATH_OAUTH2,
                        AUTHORIZATION_SERVER_PATH_WELL_KNOWN
                )
                .addOpenApiCustomizer(openApi -> openApi.info(this.info()))
                .build();
    }
}
