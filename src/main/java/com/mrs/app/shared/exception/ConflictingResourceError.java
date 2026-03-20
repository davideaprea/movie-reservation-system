package com.mrs.app.shared.exception;

import java.util.List;

public record ConflictingResourceError(
        List<?> conflictingResources,
        List<String> violatingFields,
        String reason
) {
}
