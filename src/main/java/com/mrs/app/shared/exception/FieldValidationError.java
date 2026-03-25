package com.mrs.app.shared.exception;

import java.util.List;

public record FieldValidationError(
        String fieldName,
        List<ValidationDetail> errors
) {
    public record ValidationDetail(
            String code,
            String message
    ) {
    }
}
