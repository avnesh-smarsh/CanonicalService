package com.smarsh.canonical.exception;

import com.smarsh.canonical.exception.ingestion.DuplicateMessageException;
import com.smarsh.canonical.exception.ingestion.ValidationException;
import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.exception.normalizer.StorageException;
import com.smarsh.canonical.exception.retention.DeletionException;
import com.smarsh.canonical.exception.retention.RetentionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------------- Ingestion ----------------

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationException(ValidationException ex, WebRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());
        return createErrorBody(ex.getMessage());
    }

    @ExceptionHandler(DuplicateMessageException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicateMessageException(DuplicateMessageException ex, WebRequest request) {
        log.warn("Duplicate message detected: {}", ex.getMessage());
        return createErrorBody(ex.getMessage());
    }

    // ---------------- Normalizer ----------------

    @ExceptionHandler(NormalizationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleNormalizationException(NormalizationException ex, WebRequest request) {
        log.error("Normalization error occurred: {}", ex.getMessage(), ex);
        return createErrorBody("An error occurred during message normalization.");
    }

    @ExceptionHandler(StorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleStorageException(StorageException ex, WebRequest request) {
        log.error("Storage error occurred: {}", ex.getMessage(), ex);
        return createErrorBody("An error occurred while storing the message.");
    }

    // ---------------- Retention ----------------

    @ExceptionHandler(RetentionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleRetentionException(RetentionException ex, WebRequest request) {
        log.error("Retention error occurred: {}", ex.getMessage(), ex);
        return createErrorBody(ex.getMessage());
    }

    @ExceptionHandler(DeletionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleDeletionException(DeletionException ex, WebRequest request) {
        log.error("Deletion error occurred: {}", ex.getMessage(), ex);
        return createErrorBody("An error occurred while deleting expired data.");
    }

    // ---------------- Fallback ----------------

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGlobalException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return createErrorBody("An unexpected internal error occurred.");
    }

    // ---------------- Helper ----------------

    private Map<String, Object> createErrorBody(String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return body;
    }
}
