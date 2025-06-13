package com.StoreManageBackEnd.StoreManager.data.model;

import java.util.List;

public record BatchOperationResult(
    int successCount,
    int failureCount,
    List<String> errorMessages
) {
    public boolean hasFailures() {
        return failureCount > 0;
    }
}