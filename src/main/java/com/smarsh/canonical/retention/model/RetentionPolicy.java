package com.smarsh.canonical.retention.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "retention_policies")
public class RetentionPolicy {

    @Id
    private String id;

    private String tenantId;

    private String network;

    private int retentionPeriodDays;
}
