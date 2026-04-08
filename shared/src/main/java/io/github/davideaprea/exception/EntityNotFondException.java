package io.github.davideaprea.exception;

import lombok.Getter;

@Getter
public class EntityNotFondException extends RuntimeException {
    private final EntityNotFoundError error;

    public EntityNotFondException(EntityNotFoundError error) {
        super("Couldn't find any entity with the given parameters. Details: %s".formatted(error));

        this.error = error;
    }
}
