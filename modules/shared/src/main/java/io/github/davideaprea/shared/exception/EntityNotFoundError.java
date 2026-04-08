package io.github.davideaprea.shared.exception;

public record EntityNotFoundError(
        String requestedEntityName,
        Object usedParams
) {
}
