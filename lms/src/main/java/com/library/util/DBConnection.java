package com.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Single place that knows the database credentials (replaces conn.php).
 * Credentials live in db.properties / environment variables - never in a JSP.
 */
public final class DBConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties p = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read db.properties", e);
        }
        URL = env("LIB_DB_URL", p.getProperty("db.url"));
        USER = env("LIB_DB_USER", p.getProperty("db.username", "root"));
        PASSWORD = env("LIB_DB_PASSWORD", p.getProperty("db.password", ""));
        try {
            Class.forName(p.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver not found", e);
        }
    }

    private DBConnection() {
    }

    private static String env(String name, String fallback) {
        String v = System.getenv(name);
        return (v != null && !v.isEmpty()) ? v : fallback;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
