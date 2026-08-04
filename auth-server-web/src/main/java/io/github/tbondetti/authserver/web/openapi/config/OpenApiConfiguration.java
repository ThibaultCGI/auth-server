package io.github.tbondetti.authserver.web.openapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.API_DESCRIPTION;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.API_TITLE;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.API_VERSION;
import static io.github.tbondetti.authserver.web.openapi.constants.OpenApiConstants.CONTACT_NAME;

@Configuration
public class OpenApiConfiguration {

    private Contact contact() {
        return new Contact()
                .name(CONTACT_NAME)
                ;
    }

    private Info info() {
        return new Info()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .contact(this.contact())
                ;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(this.info())
                ;
    }
}