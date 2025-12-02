package com.smarsh.canonical.normalizer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.client.RawStorageClient;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.normalizer.model.UniqueId;
import com.smarsh.canonical.normalizer.repository.NormalizerUniqueIdRepository;
import com.smarsh.canonical.normalizer.util.MessageAdapterFactory;
import com.smarsh.canonical.normalizer.util.adapters.MessageAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageAdapterFactory messageAdapterFactory;

    @Mock
    private List<StorageService> storageServices;

    @Mock
    private ProducerService producerService;

    @Mock
    private NormalizerUniqueIdRepository normalizerUniqueIdRepository;

    @Mock
    private RawStorageClient rawStorageClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageAdapter messageAdapter;

    private String validEmailJson;

    @BeforeEach
    void setUp() {
        validEmailJson = """
            {
                "stableMessageId": "stable123",
                "tenantId": "tenant1",
                "network": "email",
                "payload": {
                    "from": "sender@example.com",
                    "to": ["recipient@example.com"],
                    "subject": "Test",
                    "body": "Test body",
                    "sentAt": "2023-10-05T14:30:00Z"
                }
            }
            """;
    }

    @Test
    void processMessage_InvalidJson_ThrowsNormalizationException() {
        String invalidJson = "invalid-json";

        assertThrows(NormalizationException.class, () -> messageService.processMessage(invalidJson));
    }

}