package com.mrs.app.booking.dto.internal;

import java.math.BigDecimal;
import java.util.List;

public record PaymentDto(
        long userId,
        List<Long> selectedSeat
) {
}
