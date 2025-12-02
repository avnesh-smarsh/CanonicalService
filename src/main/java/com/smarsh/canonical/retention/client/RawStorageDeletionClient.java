package com.smarsh.canonical.retention.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarsh.canonical.retention.dto.DeleteObjectsRequest;
import com.smarsh.canonical.retention.dto.DeleteObjectsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RawStorageDeletionClient {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    /**
     * Delete raw references from Raw Storage.
     */
    public DeleteObjectsResponse deleteRawReferences(String tenantId, List<String> rawReferences, boolean dryRun) {
        try {
            log.info("Sending request to RawStorage for deletion. tenantId={}, rawReferences={}", tenantId, rawReferences);

            DeleteObjectsRequest request = new DeleteObjectsRequest();
            request.setKeys(rawReferences);
            request.setDryRun(dryRun);

            return webClientBuilder.build()
                    .post() // use POST since DELETE + body is tricky with some clients
                    .uri("http://raw-storage-service/tenants/{tenantId}/raw/objects:delete", tenantId)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(DeleteObjectsResponse.class)
                    .block();

        } catch (Exception e) {
            log.error("Failed to delete raw references for tenantId={}. {}", tenantId, e.getMessage(), e);
            throw new RuntimeException("Error deleting raw references", e);
        }
    }
}
