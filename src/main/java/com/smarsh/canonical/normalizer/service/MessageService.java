package com.smarsh.canonical.normalizer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.client.RawStorageClient;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;
import com.smarsh.canonical.normalizer.model.UniqueId;
import com.smarsh.canonical.normalizer.repository.NormalizerUniqueIdRepository;
import com.smarsh.canonical.normalizer.util.MessageAdapterFactory;
import com.smarsh.canonical.normalizer.util.adapters.MessageAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageAdapterFactory messageAdapterFactory;
    private final List<StorageService> storageServices;
    private final ProducerService producerService;
    private final NormalizerUniqueIdRepository normalizerUniqueIdRepository;
    private final RawStorageClient rawStorageClient;
    private final ObjectMapper objectMapper;

    public CanonicalMessage processMessage(String json) {
        try {
            log.info("Payload JSON: {}", json);

            JsonNode root = new ObjectMapper().readTree(json);
            String network = root.get("network").asText();

            MessageAdapter adapter = messageAdapterFactory.getAdapter(network);
            CanonicalMessage message = adapter.map(root);


            ObjectNode rawJsonNode = (ObjectNode) objectMapper.readTree(json);

            rawJsonNode.remove("stableMessageId");
            String rawJson = objectMapper.writeValueAsString(rawJsonNode);

            String rawReference=rawStorageClient.storeRaw(message,rawJson);
             message.setRawReference(rawReference);

            storageServices.forEach(storage -> storage.store(message,rawJson));
            producerService.sendMessage(message);

            normalizerUniqueIdRepository.save(new UniqueId(message.getStableMessageId()));

            return message;
        } catch (IOException e) {
            throw new NormalizationException("Error processing message in MessageService: " + e.getMessage(), e);
        }
        catch (NullPointerException e){
            log.info("Null pointer exception in MessageService");
            throw new NormalizationException("Null pointer exception in MessageService");
        }
    }
}