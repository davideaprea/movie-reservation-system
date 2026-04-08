package io.github.davideaprea.exception;

import java.util.List;

public record ConflictingResourceError<T>(
        List<T> conflictingResources,
        List<String> violatingFields,
        String reason
) {
}
