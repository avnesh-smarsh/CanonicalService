package com.smarsh.canonical.exception.normalizer;

import com.smarsh.canonical.exception.CanonicalException;

public class StorageException extends CanonicalException {

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}