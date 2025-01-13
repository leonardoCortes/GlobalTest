package com.global.evaluation.app.util;

import java.util.Base64;

public class Password {

    public static String encrypt(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}