package io.github.davideaprea.exception;

public record EntityNotFoundError(
        String requestedEntityName,
        Object usedParams
) {
}
