package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.dto.IntentCreateResponse;

public record BookingTransactionResult(
        Order order,
        BookingResponse booking,
        IntentCreateResponse intent
) {
}
