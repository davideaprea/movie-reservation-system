package com.mrs.shared.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ConflictingEntityException extends RuntimeException {
    private final static String messageTemplate = "Submitted resource is conflicting with other existing resources. Details: %s";

    private final List<?> conflictingResources;
    private final List<String> violatingFields;
    private final String reason;

    public ConflictingEntityException(List<?> conflictingResources, List<String> violatingFields, String reason) {
        super(messageTemplate.formatted(reason));
        this.conflictingResources = conflictingResources;
        this.violatingFields = violatingFields;
        this.reason = reason;
    }

    public ConflictingEntityException(List<String> violatingFields, String reason) {
        super(messageTemplate.formatted(reason));
        this.conflictingResources = List.of();
        this.violatingFields = violatingFields;
        this.reason = reason;
    }
}
