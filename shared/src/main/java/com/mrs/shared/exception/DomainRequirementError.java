package com.mrs.shared.exception;

public record DomainRequirementError(
        String reason,
        String fieldName
) {
}
