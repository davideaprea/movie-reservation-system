package com.mrs.app.payment.dto;

public record IntentSubmissionResponse(
        String id,
        String nextRequiredStep,
        String key
) {
}
