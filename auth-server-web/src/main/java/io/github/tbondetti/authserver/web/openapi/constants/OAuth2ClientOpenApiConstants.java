package io.github.tbondetti.authserver.web.openapi.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ClientOpenApiConstants {

    public static final String TAG = "OAuth2 Client";

    public static final String TAG_DESCRIPTION = "Gestion des clients OAuth2.";

    public static final String CREATE_SUMMARY = "Créer un client OAuth2";

    public static final String CREATE_DESCRIPTION = "Crée un nouveau client OAuth2.";

    public static final String CLIENT_ID_DESCRIPTION = "Identifiant du client OAuth2.";

    public static final String CLIENT_ID_EXAMPLE = "GrTDaz-4ws9g8-Krpmar-JtVqSG";

    public static final String CLIENT_NAME_DESCRIPTION = "Nom du client OAuth2.";

    public static final String CLIENT_NAME_EXAMPLE = "Test Resource Server";

    public static final String CLIENT_SECRET_DESCRIPTION = "Secret du client OAuth2.";

    @SuppressWarnings("java:S6418")
    public static final String CLIENT_SECRET_EXAMPLE = "w6icNtSsfDee1G6E09Tob4waUcja50YFIWkYXvRl4QLKAYwx6I";

    public static final String APPLICATION_CODE_DESCRIPTION = "Code de l'application du client OAuth2.";

    public static final String APPLICATION_CODE_EXAMPLE = "TRS";

    public static final String CREATE_REQUEST_DESCRIPTION = "OAuth2 client à créer.";

    public static final String RESPONSE_201_CREATED = "OAuth2 client créée.";

}
