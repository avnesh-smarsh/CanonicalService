package com.smarsh.canonical.retention.config;

import com.smarsh.canonical.retention.service.RetentionEnforcementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasksConfig {

    private final RetentionEnforcementService retentionEnforcementService;

    @Scheduled(cron = "0 0 1 * * ?") // Every day at 1:00 AM
    public void processRetentionPolicies() {
        retentionEnforcementService.enforceRetentionPolicies();
    }
}