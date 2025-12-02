package com.smarsh.canonical.normalizer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.time.Instant;

@Document(indexName = "messages")  // Elasticsearch index
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CanonicalMessage {
    @Id
    private String stableMessageId;
    private String tenantId;
    private Content content;
    private Context context;
    private String sender;
    private String network;
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Instant timestamp;
    private String rawReference;
}
