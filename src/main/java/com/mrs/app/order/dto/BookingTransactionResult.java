package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.order.entity.Order;

public record BookingTransactionResult(
        Order order,
        BookingResponse booking
) {
}
