package com.example.playlistmanager.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    // w emailu: min 1 dodwolny znak + "@" + dowolne znaki + "." + min 2 literki
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    // w haśle: min 1 w każdej rubryce -> min 8 znaków
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

    public static boolean isEmailValid(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}