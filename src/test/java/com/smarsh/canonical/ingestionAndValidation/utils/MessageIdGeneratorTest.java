package com.smarsh.canonical.ingestionAndValidation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageIdGeneratorTest {

    @InjectMocks
    private MessageIdGenerator messageIdGenerator;

    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode testPayload;

    @BeforeEach
    void setUp() throws Exception {
        String json = "{\"network\":\"email\",\"tenantId\":\"tenant123\",\"payload\":{\"from\":\"test@example.com\"}}";
        testPayload = objectMapper.readTree(json);
    }

    @Test
    void generate_SamePayload_ReturnsSameId() {
        String id1 = messageIdGenerator.generate(testPayload);
        String id2 = messageIdGenerator.generate(testPayload);

        assertEquals(id1, id2);
        assertNotNull(id1);
        assertTrue(id1.length() > 0);
    }

    @Test
    void generate_DifferentPayloads_ReturnsDifferentIds() throws Exception {
        String json2 = "{\"network\":\"email\",\"tenantId\":\"tenant123\",\"payload\":{\"from\":\"different@example.com\"}}";
        JsonNode differentPayload = objectMapper.readTree(json2);

        String id1 = messageIdGenerator.generate(testPayload);
        String id2 = messageIdGenerator.generate(differentPayload);

        assertNotEquals(id1, id2);
    }

    @Test
    void generate_ValidPayload_ReturnsBase64UrlEncodedString() {
        String id = messageIdGenerator.generate(testPayload);

        // Base64 URL encoded string should not contain +, /, or = padding
        assertFalse(id.contains("+"));
        assertFalse(id.contains("/"));
        assertFalse(id.contains("="));
    }
}