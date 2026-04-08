package io.github.davideaprea.payment.dto;

public record IntentSubmissionResponse(
        String id,
        String nextRequiredStep,
        String key
) {
}
