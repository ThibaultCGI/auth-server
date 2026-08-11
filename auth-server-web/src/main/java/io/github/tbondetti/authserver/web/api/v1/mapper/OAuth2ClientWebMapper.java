package io.github.tbondetti.authserver.web.api.v1.mapper;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2CreatedClient;
import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import io.github.tbondetti.authserver.web.api.v1.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.web.api.v1.response.OAuth2ClientResponse;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.util.stream.Collectors;

@UtilityClass
public class OAuth2ClientWebMapper {

    public static OAuth2ClientResponse toResponse(final OAuth2Client client) {
        return OAuth2ClientResponse.builder()
                .clientId(client.clientId())
                .clientName(client.clientName())
                .applicationCode(client.applicationCode())
                .grantTypes(client.grantTypes().stream().map(OAuth2ClientGrantType::getValue).collect(Collectors.toSet()))
                .redirectUris(client.redirectUris().stream().map(URI::toString).collect(Collectors.toSet()))
                .build();
    }

    public static CreateOAuth2ClientResponse toCreateResponse(final OAuth2CreatedClient client) {
        return CreateOAuth2ClientResponse.builder()
                .clientId(client.clientId())
                .clientName(client.clientName())
                .clientSecret(client.clientSecret())
                .applicationCode(client.applicationCode())
                .grantTypes(client.grantTypes().stream().map(OAuth2ClientGrantType::getValue).collect(Collectors.toSet()))
                .redirectUris(client.redirectUris().stream().map(URI::toString).collect(Collectors.toSet()))
                .build();
    }
}
