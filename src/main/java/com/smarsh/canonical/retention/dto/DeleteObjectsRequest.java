package com.smarsh.canonical.retention.dto;

import lombok.Data;
import java.util.List;
@Data
public class DeleteObjectsRequest {
    /**
     * Keys are paths *within the tenant scope*, e.g.:
     *   "slack/2025/09/06/C1/slkMsg_0001.json"
     */
    private List<String> keys;

    /**
     * If true, do not actually delete — just validate and show what *would* be deleted.
     */
    private boolean dryRun = false;
}

