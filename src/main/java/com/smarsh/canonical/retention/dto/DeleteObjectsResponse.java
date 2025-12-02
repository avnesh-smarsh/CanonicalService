package com.smarsh.canonical.retention.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeleteObjectsResponse {
    private int requested;
    private int deleted;
    private List<DeleteOutcome> results;
}

