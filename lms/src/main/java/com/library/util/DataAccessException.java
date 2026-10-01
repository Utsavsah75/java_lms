package com.library.util;

/** Unchecked wrapper for SQL problems. Never shown to the user - servlets show a friendly message. */
public class DataAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
