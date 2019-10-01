package com.klapnp.core.util;

public class Constants {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_USER = "ROLE_USER";

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    public static final String DATE_REGEX = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$";

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    /**
     * This regular expression match
     * can be used for validating strong password.
     * It expects at least
     * 1 small-case letter,
     * 1 Capital letter,
     * 1 digit,
     * 1 special character
     * and the length should be between 8-10 characters.
     * The sequence of the characters is not important.
     */
    public static final String PASSWORD_REGEX =
            "(?=^.{8,10}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$";

    /**
     * An error description when password do not match regex.
     */
    public static final String PASSWORD_REGEX_MSG = "Password should contain at least " +
            " 1 small-case letter, 1 Capital letter, 1 digit," +
            " 1 special character " +
            " and the length should be between 8-10 characters";

    /**
     * A regular expression to match phone numbers,
     * allowing for an international dialing code
     * at the start and hyphenation and spaces that are sometimes entered
     */
    public static final String INTL_PHONE_NUMBER_REGEX = "^(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*$";

    /**
     * An error description when phone number do not match regex.
     */
    public static final String INTL_PHONE_NUMBER_REGEX_MSG = "The phone number must be a valid international phone number";

    private Constants() {
    }
}
