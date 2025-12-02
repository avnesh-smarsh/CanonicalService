package com.smarsh.canonical.ingestionAndValidation.ingestioncontrollers;

import com.smarsh.canonical.ingestionAndValidation.services.MessageValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestionControllerTest {

    @Mock
    private MessageValidationService messageValidationService;

    @InjectMocks
    private MessageController messageController;

    private String validPayload;

    @BeforeEach
    void setUp() {
        validPayload = "{\"network\":\"email\",\"tenantId\":\"tenant123\",\"payload\":{\"from\":\"test@example.com\",\"to\":[\"recipient@example.com\"],\"subject\":\"Test\",\"body\":\"Test body\",\"sentAt\":\"2023-01-01T00:00:00Z\"}}";
    }

    @Test
    void ingestMessage_ValidPayload_ReturnsOk() throws Exception {
        when(messageValidationService.messageValidation(anyString()))
                .thenReturn("Message validated successfully. ID=test123");

        ResponseEntity<String> response = messageController.ingestMessage(validPayload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Message validated successfully. ID=test123", response.getBody());
        verify(messageValidationService, times(1)).messageValidation(validPayload);
    }

    @Test
    void ingestMessage_ServiceThrowsException_PropagatesException() throws Exception {
        when(messageValidationService.messageValidation(anyString()))
                .thenThrow(new RuntimeException("Validation failed"));

        assertThrows(RuntimeException.class, () -> {
            messageController.ingestMessage(validPayload);
        });
    }
}