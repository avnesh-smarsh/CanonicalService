package com.smarsh.canonical.normalizer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RawStorageClientSimpleTest {

    private RawStorageClient rawStorageClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        WebClient.Builder webClientBuilder = WebClient.builder();
        rawStorageClient = new RawStorageClient(webClientBuilder, objectMapper);
    }

    @Test
    void constructor_WithValidParameters_Succeeds() {
        assertDoesNotThrow(() -> new RawStorageClient(WebClient.builder(), new ObjectMapper()));
    }

    @Test
    void storeRaw_WithNullMessage_ThrowsException() {
        assertThrows(RuntimeException.class, () -> rawStorageClient.storeRaw(null, "{}"));
    }

    @Test
    void storeRaw_WithNullPayload_ThrowsException() {
        CanonicalMessage message = CanonicalMessage.builder().build();
        assertThrows(RuntimeException.class, () -> rawStorageClient.storeRaw(message, null));
    }

    @Test
    void storeRaw_WithInvalidJson_ThrowsException() {
        CanonicalMessage message = CanonicalMessage.builder().build();
        assertThrows(RuntimeException.class, () -> rawStorageClient.storeRaw(message, "invalid-json"));
    }
}