package com.smarsh.canonical.audits.client;

import com.smarsh.canonical.audits.dto.AuditLogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "audit-service", url = "${audit.service.url}")
public interface AuditClient {

    @PostMapping("/{tenantId}")
    void sendAuditLog(@PathVariable("tenantId") String tenantId,
                      @RequestBody AuditLogRequest request);
}
