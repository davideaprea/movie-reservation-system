package com.mrs.app.order.dto;

import com.mrs.app.payment.dto.IntentResponse;

public record OrderUpdateResponse(
        long id,
        long userId,
        IntentResponse payment
) {
}
