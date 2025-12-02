package com.smarsh.canonical.audits;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smarsh.canonical.audits.service.AuditService;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.audits.dto.AuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    // 1. Message ID Generation
    @Around("execution(* com.smarsh.canonical..*Generator.generate*(..)) && args(payload)")
    public Object auditIdGeneration(ProceedingJoinPoint pjp, JsonNode payload) throws Throwable {
        try {
            String result = (String) pjp.proceed();
            auditService.sendAuditLog(
                    payload.path("tenantId").asText("system"),
                    new AuditLogRequest(
                            result,
                            payload.path("network").asText("unknown"),
                            "ID_GENERATION_SUCCESS",
                            Map.of()
                    )
            );
            return result;
        } catch (Exception e) {
            auditService.sendAuditLog(
                    "system",
                    new AuditLogRequest(
                            "unknown",
                            "unknown",
                            "ID_GENERATION_FAILED",
                            Map.of("error", e.getMessage())
                    )
            );
            throw e;
        }
    }

    // 2. Message Validation
    @Around(value = "execution(* com.smarsh.canonical..*Validator.validate*(..)) && args(payload, messageId)", argNames = "pjp,payload,messageId")
    public Object auditValidation(ProceedingJoinPoint pjp, String payload, String messageId) throws Throwable {
        try {
            Object result = pjp.proceed();
            JsonNode root = objectMapper.readTree(payload);
            auditService.sendAuditLog(
                    root.path("tenantId").asText("system"),
                    new AuditLogRequest(
                            messageId,
                            root.path("network").asText("unknown"),
                            "VALIDATION_SUCCESS",
                            Map.of()
                    )
            );
            return result;
        } catch (Exception e) {
            JsonNode root = objectMapper.readTree(payload);
            auditService.sendAuditLog(
                    root.path("tenantId").asText("system"),
                    new AuditLogRequest(
                            messageId,
                            root.path("network").asText("unknown"),
                            "VALIDATION_FAILED",
                            Map.of("error", e.getMessage())
                    )
            );
            throw e;
        }
    }

    // 3. Duplicate Check
    @Around("execution(* com.smarsh.canonical..*DuplicateCheckService.isDuplicate(..)) && args(objectNode)")
    public Object auditDuplicateCheck(ProceedingJoinPoint pjp, ObjectNode objectNode) throws Throwable {
        String messageId = objectNode.get("stableMessageId").asText();
        String tenantId = objectNode.get("tenantId").asText();
        String network = objectNode.get("network").asText();
        try {
            boolean isDuplicate = (boolean) pjp.proceed();
            String eventType = isDuplicate ? "DUPLICATE_DETECTED" : "UNIQUE_MESSAGE";

            auditService.sendAuditLog(
                    tenantId,
                    new AuditLogRequest(
                            messageId,
                            network,
                            eventType,
                            Map.of("service", pjp.getTarget().getClass().getSimpleName())
                    )
            );
            return isDuplicate;
        } catch (Exception e) {
            auditService.sendAuditLog(
                    tenantId,
                    new AuditLogRequest(
                            messageId,
                            network,
                            "DUPLICATE_CHECK_FAILED",
                            Map.of("error", e.getMessage())
                    )
            );
            throw e;
        }
    }

    // 4. Message Processing
    @Around("execution(* com.smarsh.canonical..*Service.process*(..)) && args(json)")
    public Object auditMessageProcessing(ProceedingJoinPoint pjp, String json) throws Throwable {
        JsonNode root = new ObjectMapper().readTree(json);
        String network = root.get("network").asText();
        String tenantId = root.get("tenantId").asText();
        String messageId = root.get("stableMessageId").asText();
        try {
            Object result = pjp.proceed();
            auditService.sendAuditLog(
                    tenantId,
                    new AuditLogRequest(
                            messageId,
                            network,
                            "CANONICAL_PROCESSING_SUCCESS",
                            Map.of()
                    )
            );
            return result;
        } catch (Exception e) {
            auditService.sendAuditLog(
                    tenantId,
                    new AuditLogRequest(
                            messageId,
                            network,
                            "CANONICAL_PROCESSING_FAILED",
                            Map.of("error", e.getMessage())
                    )
            );
            throw e;
        }
    }

    // 5. Storage Operations
    @Around("execution(* com.smarsh.canonical..*StorageService.store*(..)) && args(message, ..)")
    public Object auditStorage(ProceedingJoinPoint pjp, CanonicalMessage message) throws Throwable {
        String storageType = pjp.getTarget().getClass().getSimpleName(); // Gets EsStorageService or RawStorageService
        String operation = storageType.replace("StorageService", "").toUpperCase(); // "ES" or "RAW"

        try {
            Object result = pjp.proceed();
            auditService.sendAuditLog(
                    message.getTenantId(),
                    new AuditLogRequest(
                            message.getStableMessageId(),
                            message.getNetwork(),
                            operation + "_STORAGE_SUCCESS",
                            Map.of("implementation", storageType)
                    )
            );
            return result;
        } catch (Exception e) {
            auditService.sendAuditLog(
                    message.getTenantId(),
                    new AuditLogRequest(
                            message.getStableMessageId(),
                            message.getNetwork(),
                            operation + "_STORAGE_FAILED",
                            Map.of(
                                    "implementation", storageType,
                                    "error", e.getMessage()
                            )
                    )
            );
            throw e;
        }
    }

    // 6. Message Publishing
    @Around("execution(* com.smarsh.canonical..*Producer.send*(..)) && args(message)")
    public Object auditPublishing(ProceedingJoinPoint pjp, CanonicalMessage message) throws Throwable {
        try {
            Object result = pjp.proceed();
            auditService.sendAuditLog(
                    message.getTenantId(),
                    new AuditLogRequest(
                            message.getStableMessageId(),
                            message.getNetwork(),
                            "PUBLISH_TO_KAFKA_SUCCESS",
                            Map.of("target", "KAFKA")
                    )
            );
            return result;
        } catch (Exception e) {
            auditService.sendAuditLog(
                    message.getTenantId(),
                    new AuditLogRequest(
                            message.getStableMessageId(),
                            message.getNetwork(),
                            "PUBLISH_TO_KAFKA_FAILED",
                            Map.of(
                                    "target", "KAFKA",
                                    "error", e.getMessage()
                            )
                    )
            );
            throw e;
        }
    }

    @Around("execution(* com.smarsh.canonical.normalizer.client.RawStorageClient.storeRaw(..)) && args(message,..)")
    public Object auditRawStorage(ProceedingJoinPoint pjp, CanonicalMessage message) throws Throwable {
        System.out.println("coming in aspect");
        String messageId = message.getStableMessageId();
        String tenantId = message.getTenantId();
        String network = message.getNetwork();

        try {
            Object result = pjp.proceed();

            auditService.sendAuditLog(
                    tenantId,
                    new AuditLogRequest(
                            messageId,
                            network,
                            "RAW_STORAGE_SUCCESS",
                            Map.of(
                                    "service", pjp.getTarget().getClass().getSimpleName(),
                                    "result", String.valueOf(result)
                            )
                    )
            );

            return result;
        } catch (Exception e) {
            auditService.sendAuditLog(
                    tenantId,
                    new AuditLogRequest(
                            messageId,
                            network,
                            "RAW_STORAGE_FAILED",
                            Map.of(
                                    "service", pjp.getTarget().getClass().getSimpleName(),
                                    "error", e.getMessage()
                            )
                    )
            );
            throw e; // rethrow so business flow still sees the exception
        }
    }

}