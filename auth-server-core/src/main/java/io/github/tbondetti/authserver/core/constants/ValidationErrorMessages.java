package io.github.tbondetti.authserver.core.constants;

import lombok.experimental.UtilityClass;

import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.ApplicationRules.APPLICATION_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ClientRules.CLIENT_NAME_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_CODE_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_DESCRIPTION_MAX_LENGTH;
import static io.github.tbondetti.authserver.core.constants.OAuth2ScopeRules.SCOPE_NAME_MAX_LENGTH;

@UtilityClass
public class ValidationErrorMessages {

    static final String CARACTERES = " caractères.";

    public static final String ERROR_INVALID_CREDENTIALS = "Nom d'utilisateur ou mot de passe invalide.";

    public static final String FORMAT_DONNEE_INCORRECT = "Format de donnée incorrect.";

    public static final String ERROR_CODE_IS_REQUIRED = "Le code est obligatoire.";

    public static final String ERROR_CODE_TOO_LONG = "Le code ne doit pas dépasser les %s caractères.";

    public static final String ERROR_NAME_IS_REQUIRED = "Le nom est obligatoire et ne peut pas être vide.";

    public static final String ERROR_NAME_TOO_LONG = "Le nom ne doit pas dépasser les %s caractères.";

    public static final String ERROR_DESCRIPTION_TOO_LONG = "La description ne doit pas dépasser les %s caractères.";

    /* APPLICATION */

    public static final String ERROR_APPLICATION_NOT_FOUND = "Aucune application avec le code %s n'est présente dans le référentiel.";

    public static final String ERROR_APPLICATION_CODE_MUST_BE_UNIQUE = "Le code de l'application doit être unique.";

    public static final String ERROR_APPLICATION_CODE_IS_REQUIRED = "Le code de l'application est obligatoire.";

    public static final String ERROR_APPLICATION_CODE_IS_TOO_LONG = "Le code de l'application ne doit pas dépasser les " + APPLICATION_CODE_MAX_LENGTH + CARACTERES;

    public static final String ERROR_APPLICATION_NAME_IS_REQUIRED = "Le nom de l'application est obligatoire et ne peut pas être vide.";

    public static final String ERROR_APPLICATION_NAME_IS_TOO_LONG = "Le nom de l'application ne doit pas dépasser les " + APPLICATION_NAME_MAX_LENGTH + CARACTERES;

    public static final String ERROR_APPLICATION_DESCRIPTION_IS_TOO_LONG = "La description de l'application ne doit pas dépasser les " + APPLICATION_DESCRIPTION_MAX_LENGTH + CARACTERES;

    /* CLIENT */

    public static final String ERROR_CLIENT_NOT_FOUND = "Aucun client avec client-id %s n'est présent dans le référentiel.";

    public static final String ERROR_CLIENT_ID_GENERATION_FAILED = "Impossible de générer un client ID unique après plusieurs tentatives.";

    public static final String ERROR_CLIENT_NAME_IS_REQUIRED = "Le nom du client est obligatoire.";

    public static final String ERROR_CLIENT_NAME_IS_TOO_LONG = "Le nom du client ne doit pas dépasser les " + CLIENT_NAME_MAX_LENGTH + CARACTERES;

    /* SCOPE */

    public static final String ERROR_SCOPE_NOT_FOUND = "Aucun scope avec code %s n'a été trouvé pour l'application %s";

    public static final String ERROR_SCOPE_CODE_IS_REQUIRED = "Le code du scope est obligatoire.";

    public static final String ERROR_SCOPE_CODE_TOO_LONG = "Le code du scope ne doit pas dépasser les "
            + SCOPE_CODE_MAX_LENGTH + CARACTERES;

    public static final String ERROR_SCOPE_CODE_HAS_INVALID_CARACTER = "Le code du scope ne peut contenir que des lettres "
            + "minuscules, des points (.) et des tirets (-).";

    public static final String ERROR_SCOPE_CODE_ALREADY_EXISTS_FOR_APPLICATION = "Le code du scope doit être unique pour l'application.";

    public static final String ERROR_SCOPE_NAME_IS_REQUIRED = "Le nom du scope est obligatoire.";

    public static final String ERROR_SCOPE_NAME_TOO_LONG = "Le nom du scope ne doit pas dépasser les "
            + SCOPE_NAME_MAX_LENGTH + CARACTERES;

    public static final String ERROR_SCOPE_DESCRIPTION_TOO_LONG = "La description du scope ne doit pas dépasser les "
            + SCOPE_DESCRIPTION_MAX_LENGTH + CARACTERES;

    public static final String ERROR_SCOPE_ALREADY_ASSIGNED = "Le scope %s est déjà assigné au client %s";

}
