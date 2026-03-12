package com.mrs.app.shared.exception;

import lombok.Getter;

@Getter
public class DomainRequirementException extends RuntimeException {
    private final DomainRequirementError error;

    public DomainRequirementException(DomainRequirementError error) {
        super("The submitted payload didn't meet mandatory domain requirements: %s.".formatted(error));

        this.error = error;
    }
}
