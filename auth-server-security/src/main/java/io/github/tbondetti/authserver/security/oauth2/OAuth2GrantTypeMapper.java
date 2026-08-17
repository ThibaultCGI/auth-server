package io.github.tbondetti.authserver.security.oauth2;

import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@UtilityClass
public class OAuth2GrantTypeMapper {

    public static AuthorizationGrantType toSpring(
            final OAuth2ClientGrantType grantType
    ) {
        return switch (grantType) {
            case CLIENT_CREDENTIALS -> AuthorizationGrantType.CLIENT_CREDENTIALS;
            case AUTHORIZATION_CODE -> AuthorizationGrantType.AUTHORIZATION_CODE;
            case REFRESH_TOKEN -> AuthorizationGrantType.REFRESH_TOKEN;
        };
    }
}
