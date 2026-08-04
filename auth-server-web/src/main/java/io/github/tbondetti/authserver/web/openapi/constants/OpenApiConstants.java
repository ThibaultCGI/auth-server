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
}
