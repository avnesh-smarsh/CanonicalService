package com.smarsh.canonical.normalizer.service;

import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NormalizerProcessor {

    private final MessageService messageService;

    @WithSpan
    public void process(String messageJson) {
        try {
            log.info("Processing message: {}", messageJson);
            CanonicalMessage message = messageService.processMessage(messageJson);
            log.info("Processed CanonicalMessage: {}", message);

        }catch (Exception e) {
            // Catches any other runtime exceptions
            throw new NormalizationException("Unexpected message processing failure in normalizer: " + e.getMessage(), e);
        }
    }
}