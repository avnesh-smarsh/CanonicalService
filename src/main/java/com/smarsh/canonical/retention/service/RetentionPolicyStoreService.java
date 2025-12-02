package com.smarsh.canonical.retention.service;

import com.smarsh.canonical.retention.Repository.RetentionPolicyRepository;
import com.smarsh.canonical.retention.model.RetentionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RetentionPolicyStoreService {

    private final RetentionPolicyRepository repository;

    public RetentionPolicy upsertPolicy(String tenantId, String network, int retentionDays) {
        return repository.findByTenantIdAndNetwork(tenantId, network)
                .map(existing -> {
                    existing.setRetentionPeriodDays(retentionDays);
                    return repository.save(existing);
                })
                .orElseGet(() -> repository.save(
                        RetentionPolicy.builder()
                                .tenantId(tenantId)
                                .network(network)
                                .retentionPeriodDays(retentionDays)
                                .build()
                ));
    }
    public Optional<RetentionPolicy> findByTenantIdAndNetwork(String tenantId, String network) {
        return repository.findByTenantIdAndNetwork(tenantId, network);
    }
}
