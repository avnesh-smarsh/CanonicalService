package com.smarsh.canonical.ingestionAndValidation.validation.schemaValidators;

import com.smarsh.canonical.ingestionAndValidation.validation.AbstractJsonSchemaValidator;
import org.springframework.stereotype.Component;


@Component
public class SlackMessageValidator extends AbstractJsonSchemaValidator {
    public SlackMessageValidator() {
        super("/schemas/slack-schema.json");
    }

    @Override
    public String getNetwork() {
        return "slack";
    }
}

