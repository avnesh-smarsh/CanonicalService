package com.smarsh.canonical.normalizer.util;

import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.util.adapters.MessageAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageAdapterFactory {

    private final List<MessageAdapter> adapters;

    public MessageAdapterFactory(List<MessageAdapter> adapters) {
        this.adapters = adapters;
    }

    public MessageAdapter getAdapter(String network) {
        return adapters.stream()
                .filter(a -> a.supports(network))
                .findFirst()
                // Corrected: The lambda for orElseThrow takes no arguments.
                .orElseThrow(() -> new NormalizationException("No adapter found for network: " + network));
    }
}