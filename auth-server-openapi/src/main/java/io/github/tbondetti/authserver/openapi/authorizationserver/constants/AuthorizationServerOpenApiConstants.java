package io.github.tbondetti.authserver.openapi.authorizationserver.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthorizationServerOpenApiConstants {

    /*
     * Grouped OpenAPI
     */

    public static final String AUTHORIZATION_SERVER_GROUP = "authorization-server";

    public static final String AUTHORIZATION_SERVER_DISPLAY_NAME = "OAuth2 Authorization Server API";

    public static final String AUTHORIZATION_SERVER_DESCRIPTION = """
            Endpoints standards OAuth2 et OpenID Connect.
            
            Cette API expose notamment :
            - /oauth2/token
            - /oauth2/jwks
            - /oauth2/revoke
            - /oauth2/introspect
            - /.well-known/openid-configuration
            """;

    public static final String AUTHORIZATION_SERVER_PATH_OAUTH2 = "/oauth2/**";

    public static final String AUTHORIZATION_SERVER_PATH_WELL_KNOWN = "/.well-known/**";

    /*
     * Tags
     */

    public static final String TAG = "Authorization Server";

    public static final String TAG_DESCRIPTION = """
            Endpoints standards OAuth2 et OpenID Connect.
            """;

    /*
     * Token endpoint
     */

    public static final String TOKEN_SUMMARY = "Obtenir un access token";

    public static final String TOKEN_DESCRIPTION = """
            Endpoint OAuth2 standard permettant d'obtenir un access token.
            """;

    public static final String TOKEN_REQUEST_DESCRIPTION = "Paramètres de demande du token OAuth2.";

    public static final String TOKEN_RESPONSE_200 = "Token généré avec succès.";

    /*
     * JWKS endpoint
     */

    public static final String JWKS_PATH = "/oauth2/jwks";

    public static final String JWKS_SUMMARY = "Récupérer les clés publiques";

    public static final String JWKS_DESCRIPTION = """
            Expose le jeu de clés publiques (JWKS)
            utilisé pour la validation des JWT.
            """;

    public static final String JWKS_RESPONSE_200 = "Jeu de clés publiques retourné avec succès.";

    public static final String JWKS_KEYS_DESCRIPTION = "Liste des clés publiques disponibles.";

    /*
     * OAuth2TokenRequestApi
     */

    public static final String GRANT_TYPE_DESCRIPTION = "Type de grant OAuth2.";

    public static final String GRANT_TYPE_EXAMPLE = "client_credentials";

    public static final String SCOPE_DESCRIPTION = "Scopes demandés.";

    public static final String SCOPE_EXAMPLE = "trs:resource.read";

    /*
     * OAuth2TokenResponseApi
     */

    public static final String ACCESS_TOKEN_DESCRIPTION = "Access token OAuth2.";

    @SuppressWarnings("java:S6418")
    public static final String ACCESS_TOKEN_EXAMPLE = "eyJraWQiOiI4ZGRmODk2NyIsImFsZyI6IlJTMjU2In0...";

    public static final String TOKEN_TYPE_DESCRIPTION = "Type du token.";

    public static final String TOKEN_TYPE_EXAMPLE = "Bearer";

    public static final String EXPIRES_IN_DESCRIPTION = "Durée de validité du token en secondes.";

    public static final String EXPIRES_IN_EXAMPLE = "300";

    public static final String GRANTED_SCOPE_DESCRIPTION = "Scopes accordés au client.";

    public static final String GRANTED_SCOPE_EXAMPLE = "trs:resource.read";

    /*
     * JWK
     */

    public static final String JWK_KTY_DESCRIPTION = "Type de clé.";

    public static final String JWK_KTY_EXAMPLE = "RSA";

    public static final String JWK_USE_DESCRIPTION = "Usage prévu de la clé.";

    public static final String JWK_USE_EXAMPLE = "sig";

    public static final String JWK_ALG_DESCRIPTION = "Algorithme utilisé par la clé.";

    public static final String JWK_ALG_EXAMPLE = "RS256";

    public static final String JWK_KID_DESCRIPTION = "Identifiant unique de la clé.";

    public static final String JWK_KID_EXAMPLE = "5f5b8d84-2c87-4f51-b1a9-f4cf9f7c2f65";

    public static final String JWK_N_DESCRIPTION = "Modulus RSA encodé en Base64URL.";

    public static final String JWK_N_EXAMPLE = "vT6j6jI6fK2R7z5oNn9g4hNXiC4Faf8J5I4hX9Ff...";

    public static final String JWK_E_DESCRIPTION = "Exposant public RSA encodé en Base64URL.";

    public static final String JWK_E_EXAMPLE = "AQAB";
}
