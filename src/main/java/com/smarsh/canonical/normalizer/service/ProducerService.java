package com.smarsh.canonical.normalizer.service;


import com.smarsh.canonical.exception.normalizer.StorageException;
import com.smarsh.canonical.normalizer.kafka.ComplianceProducer;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import io.micrometer.tracing.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ComplianceProducer complianceProducer;

    @WithSpan
    public void sendMessage(CanonicalMessage messageJson) {
        try {


            complianceProducer.sendMessage(messageJson);
        } catch (Exception e) {
            log.error(e.getMessage());
            // Throw a storage exception for Kafka failures
            throw new StorageException("Failed to push message to Kafka: " + e.getMessage(), e);
        }
    }
}