
package com.smarsh.canonical.ingestionAndValidation.validation;

public interface MessageValidator {
    void validate(String payload,String stableMessageId);
    String getNetwork();  // e.g. "slack", "email", "whatsapp"
}
