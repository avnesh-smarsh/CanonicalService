package com.smarsh.canonical.ingestionAndValidation.services;

import com.smarsh.canonical.exception.ingestion.DuplicateMessageException;
import com.smarsh.canonical.exception.ingestion.ValidationException;
import com.smarsh.canonical.ingestionAndValidation.validation.MessageValidator;
import com.smarsh.canonical.ingestionAndValidation.validation.ValidatorRegistry;
import com.smarsh.canonical.ingestionAndValidation.utils.MessageIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.smarsh.canonical.normalizer.service.NormalizerProcessor;
import org.slf4j.MDC;
import io.opentelemetry.instrumentation.annotations.WithSpan;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageValidationService {

    private final ValidatorRegistry validatorRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DuplicateCheckService duplicateCheckService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageIdGenerator messageIdGenerator;
    private final NormalizerProcessor normalizerProcessor;

    @WithSpan
    public String messageValidation(String payload) throws Exception {

        JsonNode root = mapper.readTree(payload);

        String network = root.path("network").asText();
        String tenantIdFromPayload = root.path("tenantId").asText();

        String stableId = messageIdGenerator.generate(root);

        MDC.put("stableMessageId", stableId); //Stable message ID insertion in logs.

        ObjectNode objectNode = (ObjectNode) root;
        objectNode.put("stableMessageId", stableId);

        try {
            MessageValidator validator = validatorRegistry.getValidator(network);
            validator.validate(payload, stableId);

            String updatedPayload = mapper.writeValueAsString(objectNode);
            log.info("updated payload: " + updatedPayload);

            // Check if duplicate and throw custom exception
            if (duplicateCheckService.isDuplicate(objectNode)) {
                throw new DuplicateMessageException("Duplicate message detected. ID=" + stableId);
            }
            log.info("no duplicate found");

            normalizerProcessor.process(updatedPayload);

            return "Message validated successfully. ID=" + stableId;

        } catch (ValidationException | IllegalArgumentException e) {
            // Only catch validation-related exceptions
            throw new ValidationException("Error validating payload for MessageId: " + stableId + ". Reason: " + e.getMessage(), e);
        } catch (DuplicateMessageException e) {
            // Let DuplicateMessageException bubble up unchanged
            throw e;
        } catch (Exception e) {
            // Handle other unexpected exceptions
            throw new ValidationException("Unexpected error validating payload for MessageId: " + stableId + ". Reason: " + e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }
}