package com.library.util;

/** A rule was broken (book unavailable, duplicate record ...). The message IS safe to show to the user. */
public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
}
