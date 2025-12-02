package com.smarsh.canonical.ingestionAndValidation.repository;

import com.smarsh.canonical.ingestionAndValidation.models.UniqueId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IngestionUniqueIdRepository extends MongoRepository<UniqueId, String> {
}

