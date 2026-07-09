package com.mrs.app.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record CinemaCreateRequest(
        @NotBlank
        String name,

        @Positive
        long addressId
) {
}
