package com.mrs.app.order.service;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.service.BookingService;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.*;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.dto.IntentCreateRequest;
import com.mrs.app.payment.dto.IntentResponse;
import com.mrs.app.payment.dto.RefundResponse;
import com.mrs.app.payment.service.PaymentService;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.service.ScheduleSeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Orchestrator for the complete order workflow.
 * <p>
 * It serves as the main entry point for order-related operations,
 * coordinating the interaction between involved modules.
 */
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final ScheduleSeatService scheduleSeatService;
    private final TransactionTemplate transactionTemplate;

    /**
     * Creates a new {@link Order}, linking the submitted booking to its intent intent.
     */
    public OrderCreateResponse create(OrderCreateRequest createRequest) {
        BookingTransactionResult result = transactionTemplate.execute(status -> {
            BookingResponse booking = bookingService.create(new BookingCreateRequest(
                    createRequest.scheduleId(),
                    createRequest.seatIds()
            ));
            BigDecimal amount = scheduleSeatService.findAllByIdIn(createRequest.seatIds())
                    .stream()
                    .map(ScheduleSeatResponse::price)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Order order = orderDAO.save(Order.builder()
                    .createdAt(LocalDateTime.now())
                    .userId(createRequest.userId())
                    .bookingId(booking.id())
                    .amount(amount)
                    .build());

            return new BookingTransactionResult(order, booking);
        });
        Order order = result.order();
        IntentCreateRequest intentCreateRequest = new IntentCreateRequest(order.getAmount(), order.getId());
        IntentResponse paymentIntent = paymentService.createConfirmedIntent(intentCreateRequest);

        order.setPaymentId(paymentIntent.id());
        orderDAO.save(order);

        return new OrderCreateResponse(order.getId(), result.booking(), paymentIntent);
    }

    /**
     * Cancels an order, issues a refund, and deletes the associated booking.
     */
    public OrderCancellationResponse cancel(OrderCancellationRequest request) {
        Order order = orderDAO
                .findById(request.orderId())
                .filter(o -> o.getUserId() == request.userId())
                .orElseThrow();
        RefundResponse refund = paymentService.refund(order.getIntentId());

        bookingService.deleteById(order.getId());

        return new OrderCancellationResponse(order.getId(), order.getUserId(), refund);
    }
}
