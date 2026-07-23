package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.infrastructure.web.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.infrastructure.web.response.OAuth2ClientResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ClientWebMapper {

    public static OAuth2ClientResponse toResponse(final OAuth2Client client) {
        return OAuth2ClientResponse
                .builder()
                .clientId(client.clientId())
                .clientName(client.clientName())
                .applicationCode(client.applicationCode())
                .build();
    }

    public static CreateOAuth2ClientResponse toCreateResponse(final OAuth2CreatedClient client) {
        return CreateOAuth2ClientResponse
                .builder()
                .clientId(client.clientId())
                .clientName(client.clientName())
                .clientSecret(client.clientSecret())
                .applicationCode(client.applicationCode())
                .build();
    }
}
