package com.smarsh.canonical.retention.controller;

import com.smarsh.canonical.retention.service.RetentionEnforcementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/retention")
@RequiredArgsConstructor
public class RetentionEnforcementController {

    private final RetentionEnforcementService enforcementService;

    /**
     * Trigger retention policy enforcement manually.
     * Example: POST /api/v1/retention/enforce
     */
    @PostMapping("/enforce")
    public String enforcePolicies() {
        log.info("Manual trigger received for retention enforcement.");
        enforcementService.enforceRetentionPolicies();
        return "Retention enforcement triggered successfully.";
    }
}
