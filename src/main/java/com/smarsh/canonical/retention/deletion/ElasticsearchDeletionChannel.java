package com.smarsh.canonical.retention.deletion;

import com.smarsh.canonical.exception.retention.DeletionException;
import com.smarsh.canonical.retention.Repository.CanonicalMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchDeletionChannel implements DeletionChannel<String> {

    private final CanonicalMessageRepository messageRepository;

    @Override
    public void delete(List<String> messageIds) {
        try {
            messageRepository.deleteAllById(messageIds);
            log.info("Deleted {} messages from Elasticsearch", messageIds.size());
        } catch (Exception e) {
            log.error("Error deleting messages from Elasticsearch : {}", e.getMessage());
            throw new DeletionException("Failed to delete messages from Elasticsearch", e);
        }
    }

}
