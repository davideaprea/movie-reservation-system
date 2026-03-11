package com.mrs.app.shared.exception;

public record EntityNotFoundError(
        String requestedEntityName,
        Object usedParams
) {
}
