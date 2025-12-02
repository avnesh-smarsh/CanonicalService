package com.smarsh.canonical.ingestionAndValidation.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smarsh.canonical.ingestionAndValidation.repository.IngestionUniqueIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuplicateCheckServiceTest {

    @Mock
    private IngestionUniqueIdRepository ingestionUniqueIdRepository;

    @InjectMocks
    private DuplicateCheckService duplicateCheckService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectNode payloadWithId;

    @BeforeEach
    void setUp() {
        payloadWithId = objectMapper.createObjectNode();
        payloadWithId.put("stableMessageId", "test123");
    }

    @Test
    void isDuplicate_ExistingId_ReturnsTrue() {
        when(ingestionUniqueIdRepository.existsById("test123")).thenReturn(true);

        boolean result = duplicateCheckService.isDuplicate(payloadWithId);

        assertTrue(result);
        verify(ingestionUniqueIdRepository).existsById("test123");
    }

    @Test
    void isDuplicate_NonExistingId_ReturnsFalse() {
        when(ingestionUniqueIdRepository.existsById("test123")).thenReturn(false);

        boolean result = duplicateCheckService.isDuplicate(payloadWithId);

        assertFalse(result);
    }

    @Test
    void isDuplicate_MissingStableMessageId_ReturnsFalse() {
        ObjectNode payloadWithoutId = objectMapper.createObjectNode();

        boolean result = duplicateCheckService.isDuplicate(payloadWithoutId);

        assertFalse(result);
        verify(ingestionUniqueIdRepository, never()).existsById(anyString());
    }
}