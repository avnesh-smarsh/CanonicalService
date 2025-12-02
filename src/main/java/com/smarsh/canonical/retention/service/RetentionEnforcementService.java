package com.smarsh.canonical.retention.service;

import com.smarsh.canonical.exception.retention.RetentionException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.retention.Repository.CanonicalMessageRepository;
import com.smarsh.canonical.retention.Repository.RetentionPolicyRepository;

import com.smarsh.canonical.retention.deletion.ElasticsearchDeletionChannel;
//import com.smarsh.canonical.retention.deletion.RawStorageDeletionChannel;
import com.smarsh.canonical.retention.model.RetentionPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetentionEnforcementService {

    private final RetentionPolicyRepository policyRepository;
    private final CanonicalMessageRepository messageRepository;
    private final ElasticsearchDeletionChannel esDeletionChannel;
//    private final RawStorageDeletionChannel rawStorageDeletionChannel;

    public void enforceRetentionPolicies() {
        log.info("Starting retention policy enforcement...");

        var policies = policyRepository.findAll();
        for (RetentionPolicy policy : policies) {
            try {
                Instant cutoff = Instant.now().minus(policy.getRetentionPeriodDays(), ChronoUnit.DAYS);

                var expiredMessages = messageRepository.findByTenantIdAndNetworkAndTimestampBefore(
                        policy.getTenantId(), policy.getNetwork(), cutoff
                );

                if (!expiredMessages.isEmpty()) {
                    var messageIds = expiredMessages.stream().map(CanonicalMessage::getStableMessageId).toList();
                    var rawRefs = expiredMessages.stream().map(CanonicalMessage::getRawReference).toList();

                    esDeletionChannel.delete(messageIds);
//                    rawStorageDeletionChannel.delete(rawRefs);

                    log.info("Retention enforcement executed for tenant={} network={} (cutoff={})",
                            policy.getTenantId(), policy.getNetwork(), cutoff);
                }
            } catch (RetentionException e) {
                log.error("Retention enforcement failed for tenant={} network={} : {}",
                        policy.getTenantId(), policy.getNetwork(), e.getMessage(), e);
                // continue with next policy
            }
        }

        log.info("Retention policy enforcement completed.");
    }
}
