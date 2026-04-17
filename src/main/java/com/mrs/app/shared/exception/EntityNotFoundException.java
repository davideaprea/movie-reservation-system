package com.mrs.app.shared.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final EntityNotFoundError error;

    public EntityNotFoundException(EntityNotFoundError error) {
        super("Couldn't find any entity with the given parameters. Details: %s".formatted(error));

        this.error = error;
    }
}
