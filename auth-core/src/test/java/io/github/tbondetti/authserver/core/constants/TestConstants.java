package io.github.tbondetti.authserver.core.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {

    public static final String GIVEN_USER_NAME = "  GIVEN_USER_NAME  ";
    public static final String GIVEN_PASSWORD = "  GIVEN_PASSWORD  ";

    public static final String USER_NAME = "USER_NAME";
    public static final String TEN_STRING_LENGTH = "1234567890";

    public static final String FIFTY_STRING_LENGTH = TEN_STRING_LENGTH
            + TEN_STRING_LENGTH
            + TEN_STRING_LENGTH
            + TEN_STRING_LENGTH
            + TEN_STRING_LENGTH;

    public static final String ONE_HUNDRED_STRING_LENGTH = FIFTY_STRING_LENGTH
            + FIFTY_STRING_LENGTH;

    public static final String TWO_HUNDRED_STRING_LENGTH = ONE_HUNDRED_STRING_LENGTH
            + ONE_HUNDRED_STRING_LENGTH;

}
