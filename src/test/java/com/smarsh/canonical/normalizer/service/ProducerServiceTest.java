package com.smarsh.canonical.normalizer.service;

import com.smarsh.canonical.exception.normalizer.StorageException;
import com.smarsh.canonical.normalizer.kafka.ComplianceProducer;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @Mock
    private ComplianceProducer complianceProducer;

    @InjectMocks
    private ProducerService producerService;

    @Test
    void sendMessage_ValidMessage_CallsComplianceProducer() {
        CanonicalMessage message = CanonicalMessage.builder()
                .stableMessageId("test123")
                .build();

        assertDoesNotThrow(() -> producerService.sendMessage(message));
        verify(complianceProducer).sendMessage(message);
    }

    @Test
    void sendMessage_ComplianceProducerThrowsException_ThrowsStorageException() {
        CanonicalMessage message = CanonicalMessage.builder().build();

        doThrow(new RuntimeException("Kafka error")).when(complianceProducer).sendMessage(message);

        StorageException exception = assertThrows(StorageException.class,
                () -> producerService.sendMessage(message));

        assertTrue(exception.getMessage().contains("Failed to push message to Kafka"));
    }
}