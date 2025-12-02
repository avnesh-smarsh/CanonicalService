package com.smarsh.canonical.retention.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class DeleteOutcome {
    private String key;     // the logical key within tenant
    private String status;  // "DELETED" | "NOT_FOUND" | "ERROR" | "SKIPPED"
    private String error;   // non-null only when status=ERROR
}

