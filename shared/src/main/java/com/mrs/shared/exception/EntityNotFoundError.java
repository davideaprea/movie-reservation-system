package com.mrs.shared.exception;

public record EntityNotFoundError(
        String requestedEntityName,
        Object usedParams
) {
}
