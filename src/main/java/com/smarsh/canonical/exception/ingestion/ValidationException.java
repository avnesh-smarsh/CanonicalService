package com.smarsh.canonical.exception.ingestion;
import com.smarsh.canonical.exception.CanonicalException;


public class ValidationException extends CanonicalException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}