package com.smarsh.canonical.ingestionAndValidation.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smarsh.canonical.exception.ingestion.DuplicateMessageException;
import com.smarsh.canonical.exception.ingestion.ValidationException;
import com.smarsh.canonical.ingestionAndValidation.validation.MessageValidator;
import com.smarsh.canonical.ingestionAndValidation.validation.ValidatorRegistry;
import com.smarsh.canonical.ingestionAndValidation.utils.MessageIdGenerator;
import com.smarsh.canonical.normalizer.service.NormalizerProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageValidationServiceTest {

    @Mock
    private ValidatorRegistry validatorRegistry;

    @Mock
    private DuplicateCheckService duplicateCheckService;

    @Mock
    private MessageIdGenerator messageIdGenerator;

    @Mock
    private NormalizerProcessor normalizerProcessor;

    @Mock
    private MessageValidator messageValidator;

    @InjectMocks
    private MessageValidationService messageValidationService;

    private String emailPayload;
    private String slackPayload;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        emailPayload = "{\"network\":\"email\",\"tenantId\":\"tenant123\",\"payload\":{\"from\":\"test@example.com\",\"to\":[\"recipient@example.com\"],\"subject\":\"Test\",\"body\":\"Test body\",\"sentAt\":\"2023-01-01T00:00:00Z\"}}";
        slackPayload = "{\"network\":\"slack\",\"tenantId\":\"tenant123\",\"user\":\"user123\",\"text\":\"Hello\",\"timestamp\":\"2023-01-01T00:00:00Z\",\"team\":\"team123\",\"channel\":\"channel123\"}";
    }

    @Test
    void messageValidation_ValidEmailMessage_ReturnsSuccess() throws Exception {
        when(validatorRegistry.getValidator("email")).thenReturn(messageValidator);
        when(messageIdGenerator.generate(any(JsonNode.class))).thenReturn("stable123");
        when(duplicateCheckService.isDuplicate(any(ObjectNode.class))).thenReturn(false);

        String result = messageValidationService.messageValidation(emailPayload);

        assertEquals("Message validated successfully. ID=stable123", result);
        verify(messageValidator).validate(anyString(), eq("stable123"));
        verify(normalizerProcessor).process(anyString());
        assertNull(MDC.get("stableMessageId")); // Should be cleared
    }

    @Test
    void messageValidation_DuplicateMessage_ThrowsDuplicateMessageException() throws Exception {
        when(validatorRegistry.getValidator("email")).thenReturn(messageValidator);
        when(messageIdGenerator.generate(any(JsonNode.class))).thenReturn("stable123");
        when(duplicateCheckService.isDuplicate(any(ObjectNode.class))).thenReturn(true);

        assertThrows(DuplicateMessageException.class, () -> {
            messageValidationService.messageValidation(emailPayload);
        });

        verify(normalizerProcessor, never()).process(anyString());
    }

    @Test
    void messageValidation_InvalidNetwork_ThrowsValidationException() throws Exception {
        String invalidPayload = "{\"network\":\"invalid\",\"tenantId\":\"tenant123\"}";
        when(validatorRegistry.getValidator("invalid")).thenThrow(new IllegalArgumentException("No validator found"));

        assertThrows(ValidationException.class, () -> {
            messageValidationService.messageValidation(invalidPayload);
        });
    }

    @Test
    void messageValidation_ValidatorThrowsException_ThrowsValidationException() throws Exception {
        when(validatorRegistry.getValidator("email")).thenReturn(messageValidator);
        when(messageIdGenerator.generate(any(JsonNode.class))).thenReturn("stable123");
        doThrow(new RuntimeException("Validation error")).when(messageValidator).validate(anyString(), anyString());

        assertThrows(ValidationException.class, () -> {
            messageValidationService.messageValidation(emailPayload);
        });
    }

}