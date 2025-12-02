/*package com.smarsh.canonical.ingestionAndValidation.Models;

import com.smarsh.canonical.ingestionAndValidation.Validation.Validator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseMessageDTO implements Validator {
    private String tenantId;

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    @Override
    public String toString() {
        return "BaseMessageDTO{" +
                "tenantId='" + tenantId + '\'' +
                '}';
    }
}*/


package com.smarsh.canonical.ingestionAndValidation.models;

import com.smarsh.canonical.ingestionAndValidation.validation.MessageValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseMessageDTO {
    private String tenantId;
    private String stableMessageId;   // <--- used for deduplication

    @Override
    public String toString() {
        return "BaseMessageDTO{" +
                "tenantId='" + tenantId + '\'' +
                ", stableMessageId='" + stableMessageId + '\'' +
                '}';
    }
}


