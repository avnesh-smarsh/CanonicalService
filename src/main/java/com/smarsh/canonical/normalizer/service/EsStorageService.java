package com.smarsh.canonical.normalizer.service;
import com.smarsh.canonical.exception.normalizer.StorageException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.normalizer.repository.MessageRepository;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EsStorageService implements StorageService {

    private final MessageRepository messageRepository;

    @WithSpan
    @Override
    public void store(CanonicalMessage message, String raw) {
        try {
            messageRepository.save(message);
            log.info("Message indexed in Elasticsearch successfully");

        } catch (Exception e) {
            log.error("Elasticsearch save failed due to: {}", e.getMessage());
            // Throw the specific storage exception
            throw new StorageException("Failed to store message in Elasticsearch: " + e.getMessage(), e);
        }
    }
}
