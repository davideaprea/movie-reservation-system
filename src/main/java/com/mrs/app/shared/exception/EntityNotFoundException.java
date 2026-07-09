package com.mrs.app.shared.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final Class<?> requestedEntity;
    private final Object usedParams;

    public EntityNotFoundException(Class<?> requestedEntity, Object usedParams) {
        super("Couldn't find any %s with the given parameters: %s".formatted(requestedEntity.getSimpleName(), usedParams));
        this.requestedEntity = requestedEntity;
        this.usedParams = usedParams;
    }
}
