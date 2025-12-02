package com.smarsh.canonical.normalizer.service;

import com.smarsh.canonical.exception.normalizer.StorageException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.normalizer.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EsStorageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private EsStorageService esStorageService;

    @Test
    void store_ValidMessage_SavesToRepository() {
        CanonicalMessage message = CanonicalMessage.builder()
                .stableMessageId("test123")
                .build();
        String raw = "raw-data";

        assertDoesNotThrow(() -> esStorageService.store(message, raw));
        verify(messageRepository).save(message);
    }

    @Test
    void store_RepositoryThrowsException_ThrowsStorageException() {
        CanonicalMessage message = CanonicalMessage.builder().build();
        String raw = "raw-data";

        doThrow(new RuntimeException("ES error")).when(messageRepository).save(message);

        StorageException exception = assertThrows(StorageException.class,
                () -> esStorageService.store(message, raw));

        assertTrue(exception.getMessage().contains("Failed to store message in Elasticsearch"));
    }
}