package io.github.tbondetti.authserver.web.openapi.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationOpenApiConstants {

    public static final String TAG = "Application";

    public static final String TAG_DESCRIPTION = "Gestion des applications.";

    public static final String GET_SUMMARY = "Récupérer une application";

    public static final String GET_DESCRIPTION = "Retourne les informations d'une application.";

    public static final String CREATE_SUMMARY = "Créer une application";

    public static final String CREATE_DESCRIPTION = "Crée une nouvelle application.";

    public static final String CODE_DESCRIPTION = "Code unique de l'application.";

    public static final String CODE_EXAMPLE = "TRS";

    public static final String NAME_DESCRIPTION = "Nom de l'application.";

    public static final String NAME_EXAMPLE = "Test Resource Server";

    public static final String DESCRIPTION_DESCRIPTION = "Description de l'application.";

    public static final String DESCRIPTION_EXAMPLE = "Application permettant de tester les APIs protégées.";

    public static final String CREATE_REQUEST_DESCRIPTION = "Application à créer.";

    public static final String CODE_PARAMETER_DESCRIPTION = "Code de l'application.";

    public static final String RESPONSE_200_OK = "Application trouvée.";

    public static final String RESPONSE_201_CREATED = "Application créée.";
}