package com.smarsh.canonical.normalizer.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RawStorageResponse {
    private String bucket;
    private String key;
    private String s3Uri;
    private String eTag;
    private String versionId;
    private String presignedGetUrl;
    private Instant presignedExpiresAt;
}

