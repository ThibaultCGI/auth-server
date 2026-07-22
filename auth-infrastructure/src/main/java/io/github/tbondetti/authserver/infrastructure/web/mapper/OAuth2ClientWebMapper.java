package io.github.tbondetti.authserver.infrastructure.web.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.infrastructure.web.response.OAuth2ClientResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ClientWebMapper {

    public static OAuth2ClientResponse toResponse(final OAuth2Client client) {
        return OAuth2ClientResponse
                .builder()
                .id(client.id())
                .clientId(client.clientId())
                .clientName(client.clientName())
                .applicationCode(client.applicationCode())
                .build();
    }
}
