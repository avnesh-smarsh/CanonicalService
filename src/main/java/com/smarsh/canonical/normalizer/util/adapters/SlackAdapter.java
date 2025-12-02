package com.smarsh.canonical.normalizer.util.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.normalizer.model.Content;
import com.smarsh.canonical.normalizer.model.Context;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SlackAdapter implements MessageAdapter {
    @Override
    public boolean supports(String network) {
        return "slack".equalsIgnoreCase(network);
    }

    @Override
    public CanonicalMessage map(JsonNode root) {
        try {
            return CanonicalMessage.builder()
                    .stableMessageId(root.get("stableMessageId").asText())
                    .tenantId(root.get("tenantId").asText())
                    .network("slack")
                    .timestamp(Instant.parse(root.get("timestamp").asText()))
                    .sender(root.get("user").asText())
                    .content(Content.builder()
                            .body(root.get("text").asText())
                            .build())
                    .context(Context.builder()
                            .team(root.get("team").asText())
                            .channel(root.get("channel").asText())
                            .build())
                    .rawReference(null)
                    .build();
        } catch (Exception e) {
            // Throw a specific NormalizationException for mapping errors
            throw new NormalizationException("Error mapping Slack message: " + e.getMessage(), e);
        }
    }
}