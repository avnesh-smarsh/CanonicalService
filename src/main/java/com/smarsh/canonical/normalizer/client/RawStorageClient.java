package com.smarsh.canonical.normalizer.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarsh.canonical.normalizer.dto.RawStorageRequest;
import com.smarsh.canonical.normalizer.dto.RawStorageResponse;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RawStorageClient {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @WithSpan
    public String storeRaw(CanonicalMessage message, String payload) {
        try {
            log.info("Sending request to RawStorage for messageId={}", message.getStableMessageId());

            JsonNode payloadNode = objectMapper.readTree(payload);

            RawStorageResponse response = webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8086/api/v1/tenants/{tenant}/raw/payload/store", message.getTenantId())
                    .bodyValue(new RawStorageRequest(
                            message.getStableMessageId(),
                            message.getNetwork(),
                            message.getTimestamp(),
                            payloadNode
                    ))
                    .retrieve()
                    .onStatus(
                            status -> status.isError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("RawStorage error: " + body))
                    )
                    .bodyToMono(RawStorageResponse.class)
                    .block();

            if (response != null) {
                log.info("RawStorage response received: key={}", response.getKey());
                return response.getKey();
            } else {
                throw new RuntimeException("RawStorage returned null response");
            }

        } catch (Exception e) {
            log.error("Failed to store raw message: {}", e.getMessage(), e);
            throw new RuntimeException("Error storing raw message", e);
        }
    }
}
