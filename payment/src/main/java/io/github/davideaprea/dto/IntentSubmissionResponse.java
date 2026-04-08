package io.github.davideaprea.dto;

public record IntentSubmissionResponse(
        String id,
        String nextRequiredStep,
        String key
) {
}
