package utils;

import java.util.regex.Pattern;

public final class Validator {

    // Prevent instantiation
    private Validator() {}

    /* =========================
       BASIC VALIDATIONS
       ========================= */

    /**
     * Check if a string is null or empty
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Check minimum length
     */
    public static boolean hasMinLength(String value, int minLength) {
        return value != null && value.trim().length() >= minLength;
    }

    /**
     * Check if string contains only letters and spaces
     */
    public static boolean isAlphabetic(String value) {
        return value != null && value.matches("[a-zA-Z ]+");
    }

    /* =========================
       EMAIL VALIDATION
       ========================= */

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /* =========================
       NUMBER VALIDATIONS
       ========================= */

    /**
     * Check if value is a valid integer
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if value is positive integer
     */
    public static boolean isPositiveInteger(String value) {
        if (!isInteger(value)) return false;
        return Integer.parseInt(value) > 0;
    }

    /**
     * Check if value is a valid double
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* =========================
       SCORE / MARK VALIDATION
       ========================= */

    /**
     * Validate exam score (0–100)
     */
    public static boolean isValidScore(String value) {
        if (!isInteger(value)) return false;
        int score = Integer.parseInt(value);
        return score >= 0 && score <= 100;
    }

    /* =========================
       DATE VALIDATION
       ========================= */

    /**
     * Validate date format yyyy-mm-dd
     */
    public static boolean isValidDate(String date) {
        return date != null &&
               date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /* =========================
       PASSWORD VALIDATION
       ========================= */

    /**
     * Validate password strength
     * - Minimum 6 characters
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
