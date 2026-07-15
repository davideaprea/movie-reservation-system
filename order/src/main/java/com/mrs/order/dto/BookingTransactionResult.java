package com.mrs.order.dto;

import com.mrs.booking.dto.BookingResponse;
import com.mrs.order.entity.Order;
import com.mrs.payment.dto.IntentCreateResponse;

public record BookingTransactionResult(
        Order order,
        BookingResponse booking,
        IntentCreateResponse intent
) {
}
