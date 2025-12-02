package com.smarsh.canonical.normalizer.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawStorageRequest {
    private String messageId;
    private String network;
    private Instant timestamp;
    private JsonNode payload;
}