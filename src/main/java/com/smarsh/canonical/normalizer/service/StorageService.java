package com.smarsh.canonical.normalizer.service;

import com.smarsh.canonical.normalizer.model.CanonicalMessage;

public interface StorageService {
    void store(CanonicalMessage message, String raw);
}
