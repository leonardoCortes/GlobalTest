package com.global.evaluation.app.util;


import java.util.regex.Pattern;

public class Validation {

    private static final String EMAIL_REGEX = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[0-9].*[0-9])[a-zA-Z0-9]{8,12}$";

    /**
     * Method to validate email rules
     * Ex:aaaaaa@domain.algo
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Mehtod to validate password rules
     * Ex: a2asFGfgjj4
     *
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
