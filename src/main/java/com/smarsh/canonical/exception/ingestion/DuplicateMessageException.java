package com.smarsh.canonical.exception.ingestion;

import com.smarsh.canonical.exception.CanonicalException;

public class DuplicateMessageException extends CanonicalException {
    public DuplicateMessageException(String message) {
        super(message);
    }
    public DuplicateMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
