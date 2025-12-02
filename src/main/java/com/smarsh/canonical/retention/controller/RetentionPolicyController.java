package com.smarsh.canonical.retention.controller;

import com.smarsh.canonical.retention.model.RetentionPolicy;
import com.smarsh.canonical.retention.service.RetentionPolicyStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retention-policies")
@RequiredArgsConstructor
public class RetentionPolicyController {

    private final RetentionPolicyStoreService retentionPolicyStoreService;

    /*
     * Upsert (create/update) retention policy for a given tenant + network.
     */
    @PutMapping("/{tenantId}/{network}")
    public ResponseEntity<RetentionPolicy> upsertPolicy(
            @PathVariable String tenantId,
            @PathVariable String network,
            @RequestBody RetentionPolicy requestBody
    ) {
        RetentionPolicy policy = retentionPolicyStoreService.upsertPolicy(
                tenantId,
                network,
                requestBody.getRetentionPeriodDays()
        );
        return ResponseEntity.ok(policy);
    }

    /*
     * Fetch policy for tenant + network.
     */
    @GetMapping("/{tenantId}/{network}")
    public ResponseEntity<RetentionPolicy> getPolicy(
            @PathVariable String tenantId,
            @PathVariable String network
    ) {
        return retentionPolicyStoreService.findByTenantIdAndNetwork(tenantId, network)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


