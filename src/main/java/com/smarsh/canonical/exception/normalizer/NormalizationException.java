package com.smarsh.canonical.exception.normalizer;

import com.smarsh.canonical.exception.CanonicalException;

public class NormalizationException extends CanonicalException {

    // This is the constructor that was missing
    public NormalizationException(String message) {
        super(message);
    }

    public NormalizationException(String message, Throwable cause) {
        super(message, cause);
    }
}