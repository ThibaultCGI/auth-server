package io.github.tbondetti.authserver.core.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ClientRules {

    public static final int CLIENT_ID_MAX_LENGTH = 100;
    public static final int CLIENT_NAME_MAX_LENGTH = 255;

    public static final int CLIENT_SECRET_MIN_LENGTH = 12;

    public static final int CLIENT_SECRET_MAX_LENGTH = 128;

}
