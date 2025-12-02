package com.smarsh.canonical.exception.retention;

import com.smarsh.canonical.exception.CanonicalException;

public class DeletionException extends CanonicalException {
    public DeletionException(String message) {
        super(message);
    }
    public DeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
