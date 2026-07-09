package com.mrs.app.shared.exception;

import lombok.Getter;

@Getter
public class DomainRequirementException extends RuntimeException {
    private final String reason;
    private final String fieldName;

    public DomainRequirementException(String reason, String fieldName) {
        super("The submitted payload didn't meet mandatory domain requirements: %s.".formatted(reason));
        this.reason = reason;
        this.fieldName = fieldName;
    }
}
