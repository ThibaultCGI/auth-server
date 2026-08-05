package io.github.tbondetti.authserver.openapi.administration.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ScopeOpenApiConstants {

    public static final String TAG = "OAuth2 Scope";

    public static final String TAG_DESCRIPTION = "Gestion des scopes OAuth2.";

    public static final String GET_SUMMARY = "Récupérer un scope OAuth2";

    public static final String GET_DESCRIPTION =
            "Retourne les informations d'un scope OAuth2.";

    public static final String CREATE_SUMMARY = "Créer un scope OAuth2";

    public static final String CREATE_DESCRIPTION =
            "Crée un nouveau scope OAuth2.";

    public static final String APPLICATION_CODE_DESCRIPTION =
            "Code de l'application du scope OAuth2.";

    public static final String APPLICATION_CODE_EXAMPLE =
            "TRS";

    public static final String SCOPE_CODE_DESCRIPTION =
            "Code du scope OAuth2.";

    public static final String SCOPE_CODE_EXAMPLE =
            "resource.read";

    public static final String SCOPE_NAME_DESCRIPTION =
            "Nom du scope OAuth2.";

    public static final String SCOPE_NAME_EXAMPLE =
            "Lecture";

    public static final String SCOPE_DESCRIPTION_DESCRIPTION =
            "Description du scope OAuth2.";

    public static final String SCOPE_DESCRIPTION_EXAMPLE =
            "Autorise la lecture des ressources.";

    public static final String CREATE_REQUEST_DESCRIPTION =
            "Scope OAuth2 à créer.";

    public static final String APPLICATION_CODE_PARAMETER_DESCRIPTION =
            "Code de l'application.";

    public static final String SCOPE_CODE_PARAMETER_DESCRIPTION =
            "Code du scope OAuth2.";

    public static final String RESPONSE_200_OK =
            "Scope OAuth2 trouvé.";

    public static final String RESPONSE_201_CREATED =
            "Scope OAuth2 créé.";
}