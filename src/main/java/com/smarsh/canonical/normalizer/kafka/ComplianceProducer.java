package com.smarsh.canonical.normalizer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ComplianceProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
   // put in application.yml/properties
    private String topic="compliance-topic";

    public ComplianceProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(CanonicalMessage message) {
        try {
            // Convert object to JSON
            String jsonMessage = objectMapper.writeValueAsString(message);

            // Send to Kafka
            kafkaTemplate.send(topic, message.getTenantId(), jsonMessage);
            log.info("Sent message to Compliance to process" + jsonMessage);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing CanonicalMessage", e);
        }
    }
}
