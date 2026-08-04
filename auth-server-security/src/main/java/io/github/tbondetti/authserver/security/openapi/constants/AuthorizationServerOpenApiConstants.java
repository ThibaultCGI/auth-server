package io.github.tbondetti.authserver.security.openapi.constants;

public class AuthorizationServerOpenApiConstants {

    public static final String API_VERSION = "1.0.0";

    public static final String AUTHORIZATION_SERVER_GROUP = "authorization-server";

    public static final String AUTHORIZATION_SERVER_PATH_OAUTH2 = "/oauth2/**";

    public static final String AUTHORIZATION_SERVER_PATH_WELL_KNOWN = "/.well-known/**";

    public static final String AUTHORIZATION_SERVER_DISPLAY_NAME = "OAuth2 Authorization Server API";

    public static final String AUTHORIZATION_SERVER_DESCRIPTION = """
        Endpoints standards OAuth2 et OpenID Connect.
        
        Cette API expose notamment :
        - /oauth2/token ;
        - /oauth2/revoke ;
        - /oauth2/introspect ;
        - /oauth2/jwks ;
        - /.well-known/openid-configuration.
        """;
}
