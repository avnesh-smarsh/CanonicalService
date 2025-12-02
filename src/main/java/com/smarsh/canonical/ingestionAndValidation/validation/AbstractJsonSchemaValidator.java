package com.smarsh.canonical.ingestionAndValidation.validation;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class AbstractJsonSchemaValidator implements MessageValidator {

    private final Schema schema;

    public AbstractJsonSchemaValidator(String schemaPath) {
        JSONObject schemaJson = new JSONObject( new JSONTokener(new InputStreamReader(this.getClass().getResourceAsStream(schemaPath), StandardCharsets.UTF_8)));
        this.schema = SchemaLoader.load(schemaJson);
    }

    @Override
    public void validate(String payload,String stableMessageId) throws ValidationException {
        JSONObject input = new JSONObject(payload);
        schema.validate(input); // throws ValidationException if invalid
    }
}
