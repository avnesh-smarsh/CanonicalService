package com.smarsh.canonical.normalizer.util.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailAdapterTest {

    private EmailAdapter emailAdapter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        emailAdapter = new EmailAdapter();
        objectMapper = new ObjectMapper();
    }

    @Test
    void supports_EmailNetwork_ReturnsTrue() {
        assertTrue(emailAdapter.supports("email"));
        assertTrue(emailAdapter.supports("EMAIL"));
    }

    @Test
    void supports_NonEmailNetwork_ReturnsFalse() {
        assertFalse(emailAdapter.supports("slack"));
        assertFalse(emailAdapter.supports("whatsapp"));
    }

    @Test
    void map_ValidEmailJson_ReturnsCanonicalMessage() throws Exception {
        String json = """
            {
                "stableMessageId": "msg123",
                "tenantId": "tenant1",
                "network": "email",
                "payload": {
                    "from": "sender@example.com",
                    "to": ["recipient1@example.com", "recipient2@example.com"],
                    "subject": "Test Subject",
                    "body": "Test body content",
                    "sentAt": "2023-10-05T14:30:00Z"
                }
            }
            """;

        JsonNode root = objectMapper.readTree(json);
        CanonicalMessage result = emailAdapter.map(root);

        assertNotNull(result);
        assertEquals("msg123", result.getStableMessageId());
        assertEquals("tenant1", result.getTenantId());
        assertEquals("email", result.getNetwork());
        assertEquals("sender@example.com", result.getSender());
        assertEquals("Test Subject", result.getContent().getSubject());
        assertEquals("Test body content", result.getContent().getBody());
        assertEquals(2, result.getContext().getRecipients().size());
        assertTrue(result.getContext().getRecipients().contains("recipient1@example.com"));
    }

    @Test
    void map_InvalidTimestampFormat_ThrowsNormalizationException() throws Exception {
        String json = """
            {
                "stableMessageId": "msg123",
                "tenantId": "tenant1",
                "network": "email",
                "payload": {
                    "from": "sender@example.com",
                    "to": ["recipient@example.com"],
                    "subject": "Test Subject",
                    "body": "Test body",
                    "sentAt": "invalid-date-format"
                }
            }
            """;

        JsonNode root = objectMapper.readTree(json);

        assertThrows(NormalizationException.class, () -> emailAdapter.map(root));
    }
}