package com.smarsh.canonical.audits.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
public class AuditLogRequest {
    private String messageId;

    private String network;

    private String eventType;

    private String service;
    private Instant timestamp;
    private Map<String, Object> details;

    public AuditLogRequest(String messageId, String network, String eventType,Map<String, Object> details) {
        this.messageId = messageId;
        this.network = network;
        this.eventType = eventType;
        this.service = "CANONICAL-SERVICE";
        this.timestamp = Instant.now();
        this.details = details;
    }

}
