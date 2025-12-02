package com.smarsh.canonical.normalizer.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "uniqueIDs")
public class UniqueId {

    @Id
    private String id; // messageId

    public UniqueId(String id) {
        this.id = id;
    }

}
