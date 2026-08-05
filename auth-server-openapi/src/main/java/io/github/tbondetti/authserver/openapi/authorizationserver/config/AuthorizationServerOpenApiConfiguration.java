package io.github.tbondetti.authserver.openapi.authorizationserver.config;

import io.github.tbondetti.authserver.openapi.authorizationserver.response.JwksResponseApi;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_DISPLAY_NAME;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_GROUP;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_PATH_OAUTH2;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.AUTHORIZATION_SERVER_PATH_WELL_KNOWN;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWKS_DESCRIPTION;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWKS_PATH;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWKS_RESPONSE_200;
import static io.github.tbondetti.authserver.openapi.authorizationserver.constants.AuthorizationServerOpenApiConstants.JWKS_SUMMARY;
import static io.github.tbondetti.authserver.openapi.common.constants.OpenApiConstants.API_VERSION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class AuthorizationServerOpenApiConfiguration {

    private Schema<?> schema() {
        return new Schema<JwksResponseApi>()
                .$ref("#/components/schemas/JwksResponseApi")
                ;
    }
    private MediaType mediaType() {
        return new MediaType()
                .schema(this.schema())
                ;
    }

    private Content content() {
        return new Content()
                .addMediaType(APPLICATION_JSON_VALUE, this.mediaType())
                ;
    }

    private ApiResponse apiResponse() {
        return new ApiResponse()
                .description(JWKS_RESPONSE_200)
                .content(this.content())
                ;
    }

    private ApiResponses apiResponses() {
        return new ApiResponses()
                .addApiResponse("200", this.apiResponse())
                ;
    }

    private Operation operation() {
        return new Operation()
                .summary(JWKS_SUMMARY)
                .description(JWKS_DESCRIPTION)
                .responses(this.apiResponses())
                ;

    }

    private PathItem pathItem() {
        return new PathItem()
                .get(this.operation())
                ;
    }

    private OpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.path(JWKS_PATH, this.pathItem());
    }

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
                .addOpenApiCustomizer(this.openApiCustomizer())
                .build();
    }
}
