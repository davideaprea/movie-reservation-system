package com.mrs.app.payment.dto;

import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;

public record IntentCreateResponse(
        GatewayIntentCreateResponse gatewayResponse,
        IntentResponse serviceResponse
) {
}
