package com.smarsh.canonical.normalizer.util.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.normalizer.model.Content;
import com.smarsh.canonical.normalizer.model.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EmailAdapter implements MessageAdapter {

    @Override
    public boolean supports(String network) {
        return "email".equalsIgnoreCase(network);
    }

    @Override
    public CanonicalMessage map(JsonNode root) {
        try {
            JsonNode payload = root.get("payload");

            // Sender
            String sender = payload.get("from").asText();

            // Recipients
            List<String> recipients = new ArrayList<>();
            payload.get("to").forEach(toNode -> recipients.add(toNode.asText()));

            // Build message
            return CanonicalMessage.builder()
                    .stableMessageId(root.get("stableMessageId").asText())
                    .tenantId(root.get("tenantId").asText())
                    .network("email")
                    .timestamp(Instant.parse(payload.get("sentAt").asText()))
                    .sender(sender)
                    .content(Content.builder()
                            .subject(payload.get("subject").asText())
                            .body(payload.get("body").asText())
                            .build())
                    .context(Context.builder()
                            .recipients(recipients)
                            .build())
                    .rawReference(null)
                    .build();

        } catch (Exception e) {
            log.error("Failed to map Email message. Raw input: {}", root, e);
            // Throw a specific NormalizationException
            throw new NormalizationException("Error mapping Email message: " + e.getMessage(), e);
        }
    }
}
