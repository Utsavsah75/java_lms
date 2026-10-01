package com.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Reads app.properties (classpath) with optional environment-variable overrides. */
public final class AppConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read app.properties", e);
        }
    }

    private AppConfig() {
    }

    public static String get(String key, String defaultValue) {
        String env = System.getenv("LIB_" + key.toUpperCase().replace('.', '_'));
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        String v = PROPS.getProperty(key);
        return (v == null || v.isBlank()) ? defaultValue : v.trim();
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }
}
