package com.smarsh.canonical.normalizer.util.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.smarsh.canonical.normalizer.model.CanonicalMessage;

public interface MessageAdapter {
    boolean supports(String network);
    CanonicalMessage map(JsonNode root);
}
