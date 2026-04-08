package io.github.davideaprea.shared.exception;

public record DomainRequirementError(
        String reason,
        String fieldName
) {
}
