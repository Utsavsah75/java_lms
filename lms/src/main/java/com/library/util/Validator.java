package com.library.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/** Small, dependency-free input validation helpers. */
public final class Validator {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^[0-9]{7,15}$");
    private static final Pattern ISBN = Pattern.compile("^[A-Za-z0-9._-]{1,25}$");
    private static final Pattern STUDENT_CODE = Pattern.compile("^[A-Za-z0-9_-]{1,20}$");

    private Validator() {
    }

    public static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isEmail(String s) {
        return s != null && s.length() <= 255 && EMAIL.matcher(s).matches();
    }

    public static boolean isPhone(String s) {
        return s != null && PHONE.matcher(s).matches();
    }

    public static boolean isIsbn(String s) {
        return s != null && ISBN.matcher(s).matches();
    }

    public static boolean isStudentCode(String s) {
        return s != null && STUDENT_CODE.matcher(s).matches();
    }

    public static boolean maxLen(String s, int max) {
        return s != null && s.length() <= max;
    }

    public static boolean isPositiveInt(String s) {
        try {
            return Integer.parseInt(s.trim()) > 0;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isNonNegativeInt(String s) {
        try {
            return Integer.parseInt(s.trim()) >= 0;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isMoney(String s) {
        try {
            return new java.math.BigDecimal(s.trim()).signum() >= 0 && s.trim().length() <= 10;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isDate(String s) {
        try {
            LocalDate.parse(s);
            return true;
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    /** Password rule used for every new password: at least 6 characters. */
    public static boolean isStrongEnough(String pw) {
        return pw != null && pw.length() >= 6 && pw.length() <= 100;
    }

    public static int toInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (RuntimeException e) {
            return fallback;
        }
    }
}
