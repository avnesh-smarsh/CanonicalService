// services/DuplicateCheckService.java
package com.smarsh.canonical.ingestionAndValidation.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smarsh.canonical.ingestionAndValidation.repository.IngestionUniqueIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DuplicateCheckService {

    private final IngestionUniqueIdRepository ingestionUniqueIdRepository;

    public boolean isDuplicate(ObjectNode payload) {
        if(payload.get("stableMessageId") == null) {
            return false;
        }
        return ingestionUniqueIdRepository.existsById(payload.get("stableMessageId").asText());
    }
}
