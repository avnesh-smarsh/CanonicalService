package com.smarsh.canonical.normalizer.repository;

import com.smarsh.canonical.normalizer.model.UniqueId;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface NormalizerUniqueIdRepository extends MongoRepository<UniqueId, String> {

}
