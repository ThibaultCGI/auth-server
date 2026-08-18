package io.github.tbondetti.authserver.security.oauth2;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.domain.OAuth2Scope;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

@UtilityClass
public class OAuth2RegisteredClientMapper {

    private static final Duration ACCESS_TOKEN_TIME_TO_LIVE = Duration.ofMinutes(5);
    private static final Duration REFRESH_TOKEN_TIME_TO_LIVE = Duration.ofDays(30);

    static final TokenSettings TOKEN_SETTINGS = TokenSettings.builder()
            .accessTokenTimeToLive(ACCESS_TOKEN_TIME_TO_LIVE)
            .refreshTokenTimeToLive(REFRESH_TOKEN_TIME_TO_LIVE)
            .reuseRefreshTokens(true) // le refresh token reste inchangé après un refresh
            .build()
        ;

    public static RegisteredClient toRegisteredClient(
            final OAuth2Client client,
            final List<OAuth2Scope> scopes
    ) {
        final RegisteredClient.Builder builder = RegisteredClient.withId(client.id().toString())
                .clientId(client.clientId())
                .clientSecret(client.clientSecretHash())
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                .tokenSettings(TOKEN_SETTINGS)
                ;

        scopes.stream().map(OAuth2Scope::completeCode).forEach(builder::scope);

        client.grantTypes().stream().map(OAuth2GrantTypeMapper::toSpring).forEach(builder::authorizationGrantType);

        client.redirectUris().stream().map(URI::toString).forEach(builder::redirectUri);

        return builder.build();
    }
}
