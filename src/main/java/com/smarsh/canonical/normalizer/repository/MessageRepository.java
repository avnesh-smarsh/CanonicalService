package com.smarsh.canonical.normalizer.repository;

import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageRepository extends ElasticsearchRepository<CanonicalMessage, String> {
}
