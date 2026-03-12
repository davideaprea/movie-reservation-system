package com.mrs.app.shared.exception;

public record DomainRequirementError(
        String reason,
        String fieldName
) {
}
