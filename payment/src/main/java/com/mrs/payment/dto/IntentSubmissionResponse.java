package com.mrs.payment.dto;

public record IntentSubmissionResponse(
        String id,
        String nextRequiredStep,
        String key
) {
}
