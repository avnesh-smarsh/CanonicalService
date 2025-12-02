package com.smarsh.canonical.exception.retention;

import com.smarsh.canonical.exception.CanonicalException;

public class RetentionException extends CanonicalException {
    public RetentionException(String message) {
        super(message);
    }
    public RetentionException(String message, Throwable cause) {
        super(message, cause);
    }
}

