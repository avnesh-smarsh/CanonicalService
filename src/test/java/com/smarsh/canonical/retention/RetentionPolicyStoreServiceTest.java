package com.smarsh.canonical.retention.service;

import com.smarsh.canonical.retention.Repository.RetentionPolicyRepository;
import com.smarsh.canonical.retention.model.RetentionPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetentionPolicyStoreServiceTest {

    @Mock
    private RetentionPolicyRepository repository;

    @InjectMocks
    private RetentionPolicyStoreService service;


    @Test
    void upsertPolicy_WhenPolicyNotExists_CreatesNewPolicy() {
        // Given
        when(repository.findByTenantIdAndNetwork("tenant1", "network1"))
                .thenReturn(Optional.empty());

        // When
        service.upsertPolicy("tenant1", "network1", 30);

        // Then
        verify(repository).save(argThat(policy ->
                policy.getTenantId().equals("tenant1") &&
                        policy.getNetwork().equals("network1") &&
                        policy.getRetentionPeriodDays() == 30
        ));
    }

    @Test
    void findByTenantIdAndNetwork_WhenExists_ReturnsPolicy() {
        // Given
        RetentionPolicy policy = RetentionPolicy.builder()
                .tenantId("tenant1")
                .network("network1")
                .retentionPeriodDays(30)
                .build();

        when(repository.findByTenantIdAndNetwork("tenant1", "network1"))
                .thenReturn(Optional.of(policy));

        // When
        Optional<RetentionPolicy> result = service.findByTenantIdAndNetwork("tenant1", "network1");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTenantId()).isEqualTo("tenant1");
        assertThat(result.get().getNetwork()).isEqualTo("network1");
    }

    @Test
    void findByTenantIdAndNetwork_WhenNotExists_ReturnsEmpty() {
        // Given
        when(repository.findByTenantIdAndNetwork("tenant1", "network1"))
                .thenReturn(Optional.empty());

        // When
        Optional<RetentionPolicy> result = service.findByTenantIdAndNetwork("tenant1", "network1");

        // Then
        assertThat(result).isEmpty();
    }
}