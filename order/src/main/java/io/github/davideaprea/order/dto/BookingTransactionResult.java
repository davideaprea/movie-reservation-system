package io.github.davideaprea.order.dto;

import io.github.davideaprea.booking.dto.BookingResponse;
import io.github.davideaprea.order.entity.Order;
import io.github.davideaprea.payment.dto.IntentResponse;

public record BookingTransactionResult(
        Order order,
        BookingResponse booking,
        IntentResponse intent
) {
}
