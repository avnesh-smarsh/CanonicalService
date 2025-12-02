package com.smarsh.canonical.retention.Repository;

import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.time.Instant;
import java.util.List;

//@Repository
public interface CanonicalMessageRepository extends ElasticsearchRepository<CanonicalMessage, String> {

    List<CanonicalMessage> findByTenantIdAndNetworkAndTimestampBefore(String tenantId, String network, Instant timestamp);
}





