package com.smarsh.canonical.exception;

public class CanonicalException extends RuntimeException {

    public CanonicalException(String message) {
        super(message);
    }

    public CanonicalException(String message, Throwable cause) {
        super(message, cause);
    }
}