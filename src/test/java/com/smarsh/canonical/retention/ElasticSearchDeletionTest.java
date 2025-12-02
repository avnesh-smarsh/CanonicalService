package com.smarsh.canonical.retention.deletion;

import com.smarsh.canonical.exception.retention.DeletionException;
import com.smarsh.canonical.retention.Repository.CanonicalMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElasticSearchDeletionTest {

    @Mock
    private CanonicalMessageRepository messageRepository;

    @InjectMocks
    private ElasticsearchDeletionChannel channel;

    @Test
    void delete_WithValidMessageIds_DeletesFromRepository() {
        // Given
        List<String> messageIds = List.of("msg1", "msg2", "msg3");

        // When
        channel.delete(messageIds);

        // Then
        verify(messageRepository).deleteAllById(messageIds);
    }

    @Test
    void delete_WithRepositoryException_ThrowsDeletionException() {
        // Given
        List<String> messageIds = List.of("msg1");
        doThrow(new RuntimeException("DB error"))
                .when(messageRepository).deleteAllById(messageIds);

        // When/Then
        assertThrows(DeletionException.class, () -> channel.delete(messageIds));
    }

}