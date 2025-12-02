package com.smarsh.canonical.retention.Repository;

import com.smarsh.canonical.retention.model.RetentionPolicy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface RetentionPolicyRepository extends MongoRepository<RetentionPolicy, String> {
    Optional<RetentionPolicy> findByTenantIdAndNetwork(String tenantId, String network);
}
