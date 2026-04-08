package io.github.davideaprea.exception;

public record DomainRequirementError(
        String reason,
        String fieldName
) {
}
