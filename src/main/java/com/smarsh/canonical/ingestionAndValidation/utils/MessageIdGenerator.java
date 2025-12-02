// utils/MessageIdGenerator.java
package com.smarsh.canonical.ingestionAndValidation.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;



@Slf4j
@Service
public class MessageIdGenerator {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public  String generate(JsonNode payload) {
        try {
            String json = objectMapper.writeValueAsString(payload); // stable JSON
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            log.error("Error generating stableMessageId", e);
            throw new RuntimeException("Error generating stableMessageId", e);
        }
    }
}

