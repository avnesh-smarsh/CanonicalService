package com.smarsh.canonical.ingestionAndValidation.validation.schemaValidators;

import com.smarsh.canonical.ingestionAndValidation.validation.AbstractJsonSchemaValidator;
import org.springframework.stereotype.Component;


@Component
public class EmailMessageValidator extends AbstractJsonSchemaValidator {
    public EmailMessageValidator() {
        super("/schemas/email-schema.json");
    }

    @Override
    public String getNetwork() {
        return "email";
    }
}

