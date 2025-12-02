package com.smarsh.canonical.audits.service;


import com.smarsh.canonical.audits.client.AuditClient;
import com.smarsh.canonical.audits.dto.AuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditClient auditClient;

    public void sendAuditLog(String tenantId, AuditLogRequest request) {
        try {
            auditClient.sendAuditLog(tenantId, request);
        } catch (Exception e) {
            System.err.println("Async audit failed for tenant " + tenantId + ": " + e.getMessage());
        }
    }
}