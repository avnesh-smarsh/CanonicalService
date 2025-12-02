package com.smarsh.canonical.normalizer.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Context {
    private String team;
    private String channel;
    private List<String> recipients;
}
