package com.mrs.app.shared.exception;

import lombok.Getter;

@Getter
public class ConflictingEntityException extends RuntimeException {
    public final ConflictingResourceError error;

    public ConflictingEntityException(ConflictingResourceError error) {
        super("Submitted resource is conflicting with other existing resources. Details: %s".formatted(error));

        this.error = error;
    }
}
