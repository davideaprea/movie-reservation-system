package com.mrs.app.shared.exception;

import java.util.List;

public record ConflictingResourceError(
        List<? extends Record> conflictingResources,
        List<String> violatingFields,
        String reason
) {
}
