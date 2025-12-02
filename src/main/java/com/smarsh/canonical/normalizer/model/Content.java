package com.smarsh.canonical.normalizer.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Content {
    private String subject;
    private String body;
}
