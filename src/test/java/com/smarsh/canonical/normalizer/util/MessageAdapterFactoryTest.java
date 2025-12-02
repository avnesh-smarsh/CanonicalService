package com.smarsh.canonical.normalizer.util;

import com.smarsh.canonical.exception.normalizer.NormalizationException;
import com.smarsh.canonical.normalizer.util.adapters.EmailAdapter;
import com.smarsh.canonical.normalizer.util.adapters.MessageAdapter;
import com.smarsh.canonical.normalizer.util.adapters.SlackAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageAdapterFactoryTest {

    private MessageAdapterFactory factory;
    private EmailAdapter emailAdapter;
    private SlackAdapter slackAdapter;

    @BeforeEach
    void setUp() {
        emailAdapter = new EmailAdapter();
        slackAdapter = new SlackAdapter();
        factory = new MessageAdapterFactory(List.of(emailAdapter, slackAdapter));
    }

    @Test
    void getAdapter_EmailNetwork_ReturnsEmailAdapter() {
        MessageAdapter adapter = factory.getAdapter("email");
        assertInstanceOf(EmailAdapter.class, adapter);
    }

    @Test
    void getAdapter_SlackNetwork_ReturnsSlackAdapter() {
        MessageAdapter adapter = factory.getAdapter("slack");
        assertInstanceOf(SlackAdapter.class, adapter);
    }

    @Test
    void getAdapter_NetworkCaseInsensitive_ReturnsCorrectAdapter() {
        MessageAdapter emailAdapter = factory.getAdapter("EMAIL");
        MessageAdapter slackAdapter = factory.getAdapter("SLACK");

        assertInstanceOf(EmailAdapter.class, emailAdapter);
        assertInstanceOf(SlackAdapter.class, slackAdapter);
    }

    @Test
    void getAdapter_UnknownNetwork_ThrowsNormalizationException() {
        assertThrows(NormalizationException.class, () -> factory.getAdapter("unknown"));
    }
}