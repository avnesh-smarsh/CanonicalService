package com.smarsh.canonical.normalizer.service;

import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NormalizerProcessorTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private NormalizerProcessor normalizerProcessor;

    @Test
    void process_ValidMessage_CallsMessageService() {
        String validJson = "{}";
        CanonicalMessage mockMessage = CanonicalMessage.builder().build();

        when(messageService.processMessage(validJson)).thenReturn(mockMessage);

        assertDoesNotThrow(() -> normalizerProcessor.process(validJson));
        verify(messageService).processMessage(validJson);
    }

    @Test
    void process_MessageServiceThrowsException_ThrowsNormalizationException() {
        String invalidJson = "{}";

        when(messageService.processMessage(invalidJson))
                .thenThrow(new RuntimeException("Processing failed"));

        NormalizationException exception = assertThrows(NormalizationException.class,
                () -> normalizerProcessor.process(invalidJson));

        assertTrue(exception.getMessage().contains("Unexpected message processing failure"));
    }
}