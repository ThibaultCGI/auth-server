package io.github.tbondetti.authserver.web.openapi.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OpenApiConstants {

    public static final String API_TITLE = "Auth Server API";

    public static final String API_DESCRIPTION = """
            API Auth Server utilisée pour la gestion des clients et scopes.
            """;

    public static final String API_VERSION = "1.0.0";

    public static final String CONTACT_NAME = "Thibault BONDETTI";

    public static final String SECURITY_SCHEME_NAME = "basicAuth";

    public static final String SECURITY_SCHEME_SCHEME = "basic";

    public static final String RESPONSE_400_BAD_REQUEST = "Requête invalide.";

    public static final String RESPONSE_401_UNAUTHORIZED = "Authentification requise.";

    public static final String RESPONSE_403_FORBIDDEN = "Accès refusé.";

    public static final String RESPONSE_500_INTERNAL_SERVER_ERROR = "Erreur interne du serveur.";

    public static final String API_ERROR_RESPONSE_CODE_DESCRIPTION = "Code fonctionnel";

    public static final String API_ERROR_RESPONSE_CODE_EXAMPLE = "CLIENT_NOT_FOUND";

    public static final String API_ERROR_RESPONSE_DESCRIPTION_DESCRIPTION = "Description de l'erreur";

    public static final String API_ERROR_RESPONSE_DESCRIPTION = "Format standard des réponses en erreur.";

    public static final String OAUTH2_ADMINISTRATION_GROUP = "oauth2-admin";

    public static final String OAUTH2_ADMINISTRATION_PATH_APPLICATIONS = "/api/v1/applications/**";

    public static final String OAUTH2_ADMINISTRATION_PATH_CLIENTS = "/api/v1/clients/**";

    public static final String OAUTH2_ADMINISTRATION_PATH_SCOPES = "/api/v1/scopes/**";

    public static final String AUTHORIZATION_SERVER_GROUP = "authorization-server";

    public static final String AUTHORIZATION_SERVER_PATH_OAUTH2 = "/oauth2/**";

    public static final String AUTHORIZATION_SERVER_PATH_WELL_KNOWN = "/.well-known/**";

    public static final String IAM_ADMINISTRATION_GROUP = "iam-admin";

    public static final String IAM_ADMINISTRATION_PATH_USERS = "/api/v1/users/**";

    public static final String IAM_ADMINISTRATION_PATH_ROLES = "/api/v1/roles/**";

    public static final String OAUTH2_ADMINISTRATION_DISPLAY_NAME = "OAuth2 Administration API";

    public static final String OAUTH2_ADMINISTRATION_DESCRIPTION = """
        API d'administration OAuth2.
        
        Permet de gérer :
        - les applications ;
        - les clients OAuth2 ;
        - les scopes OAuth2.
        """;

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

    public static final String IAM_ADMINISTRATION_DISPLAY_NAME = "IAM Administration API";

    public static final String IAM_ADMINISTRATION_DESCRIPTION = """
        API d'administration IAM.
        
        Permet de gérer :
        - les utilisateurs ;
        - les rôles ;
        - les habilitations.
        """;
}
