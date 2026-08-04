package io.github.tbondetti.authserver.openapi.iam.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.API_VERSION;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.IAM_ADMINISTRATION_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.IAM_ADMINISTRATION_DISPLAY_NAME;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.IAM_ADMINISTRATION_GROUP;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.IAM_ADMINISTRATION_PATH_ROLES;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.IAM_ADMINISTRATION_PATH_USERS;

@Configuration
public class IamAdministrationOpenApiConfiguration {

    private Info info() {
        return new Info()
                .title(IAM_ADMINISTRATION_DISPLAY_NAME)
                .description(IAM_ADMINISTRATION_DESCRIPTION)
                .version(API_VERSION)
                ;
    }

    @Bean
    public GroupedOpenApi iamAdministrationApi() {
        return GroupedOpenApi.builder()
                .group(IAM_ADMINISTRATION_GROUP)
                .displayName(IAM_ADMINISTRATION_DISPLAY_NAME)
                .pathsToMatch(
                        IAM_ADMINISTRATION_PATH_USERS,
                        IAM_ADMINISTRATION_PATH_ROLES
                )
                .addOpenApiCustomizer(openApi -> openApi.info(this.info()))
                .build();
    }
}
