package io.github.tbondetti.authserver.core.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ClientRules {

    public static final int CLIENT_ID_LENGTH = 27; // 6 * 4 + 3

    public static final int CLIENT_NAME_MAX_LENGTH = 255;

    public static final int CLIENT_SECRET_MAX_LENGTH = 50;
}
