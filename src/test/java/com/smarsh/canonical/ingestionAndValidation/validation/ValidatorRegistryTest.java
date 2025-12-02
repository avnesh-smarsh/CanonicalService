package com.smarsh.canonical.ingestionAndValidation.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorRegistryTest {

    @Mock
    private MessageValidator emailValidator;

    @Mock
    private MessageValidator slackValidator;

    private ValidatorRegistry validatorRegistry;

    @BeforeEach
    void setUp() {
        when(emailValidator.getNetwork()).thenReturn("email");
        when(slackValidator.getNetwork()).thenReturn("slack");

        validatorRegistry = new ValidatorRegistry(emailValidator, slackValidator);
    }

    @Test
    void getValidator_ExistingNetwork_ReturnsValidator() {
        MessageValidator result = validatorRegistry.getValidator("email");

        assertEquals(emailValidator, result);
    }

    @Test
    void getValidator_NetworkCaseInsensitive_ReturnsValidator() {
        MessageValidator result1 = validatorRegistry.getValidator("EMAIL");
        MessageValidator result2 = validatorRegistry.getValidator("Email");

        assertEquals(emailValidator, result1);
        assertEquals(emailValidator, result2);
    }

    @Test
    void getValidator_NonExistingNetwork_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            validatorRegistry.getValidator("whatsapp");
        });
    }
}