package com.mrs.app.order.service;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.service.BookingService;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.*;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.dto.CompletionResponse;
import com.mrs.app.payment.dto.IntentCreateRequest;
import com.mrs.app.payment.dto.IntentResponse;
import com.mrs.app.payment.dto.RefundResponse;
import com.mrs.app.payment.service.PaymentService;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.service.ScheduleSeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final ScheduleSeatService scheduleSeatService;

    public OrderCreateResponse create(OrderCreateRequest createRequest) {
        BigDecimal totalPrice = scheduleSeatService.findAllByIdIn(createRequest.seatIds())
                .stream()
                .map(ScheduleSeatResponse::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BookingResponse booking = bookingService.create(new BookingCreateRequest(createRequest.scheduleId(), createRequest.seatIds()));
        IntentResponse paymentIntent = paymentService.create(new IntentCreateRequest(createRequest.userId(), totalPrice));
        Order order = orderDAO.save(new Order(null, paymentIntent.id(), createRequest.userId(), booking.id()));

        return new OrderCreateResponse(order.getId(), booking, paymentIntent);
    }

    public OrderCompletionResponse complete(OrderUpdateRequest request) {
        Order order = orderDAO
                .findById(request.orderId())
                .filter(o -> o.getUserId() == request.userId())
                .orElseThrow();
        CompletionResponse paymentCompletion = paymentService.complete(order.getIntentId());

        return new OrderCompletionResponse(order.getId(), order.getUserId(), paymentCompletion);
    }

    public OrderCancellationResponse cancel(OrderUpdateRequest request) {
        Order order = orderDAO
                .findById(request.orderId())
                .filter(o -> o.getUserId() == request.userId())
                .orElseThrow();
        RefundResponse refund = paymentService.refund(order.getIntentId());

        bookingService.deleteById(order.getId());

        return new OrderCancellationResponse(order.getId(), order.getUserId(), refund);
    }
}
