package com.mrs.app.shared.exception;

public class EntityNotFondException extends RuntimeException {
    public EntityNotFondException(String entityName, Record usedParams) {
        super("Couldn't find any %s with the given parameters: %s".formatted(entityName, usedParams));
    }

    public EntityNotFondException(String entityName, Long id) {
        super("Couldn't find any %s with the given id: %s".formatted(entityName, id));
    }
}
