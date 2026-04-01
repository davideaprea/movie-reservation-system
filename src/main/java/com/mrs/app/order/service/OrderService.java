package com.mrs.app.order.service;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.service.BookingService;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.*;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.dto.IntentCreateRequest;
import com.mrs.app.payment.dto.IntentCreateResponse;
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
     * Creates a new {@link Order}, linking the submitted booking to its intent.
     */
    public OrderCreateResponse create(OrderCreateRequest createRequest) {
        BookingTransactionResult result = transactionTemplate.execute(status -> {
            BookingResponse booking = bookingService.create(new BookingCreateRequest(
                    createRequest.scheduleId(),
                    createRequest.seatIds()
            ));
            Order order = orderDAO.save(Order.builder()
                    .createdAt(LocalDateTime.now())
                    .userId(createRequest.userId())
                    .bookingId(booking.id())
                    .build());

            return new BookingTransactionResult(order, booking);
        });
        Order order = result.order();
        BigDecimal amount = scheduleSeatService.findAllByIdIn(createRequest.seatIds())
                .stream()
                .map(ScheduleSeatResponse::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        IntentCreateRequest intentCreateRequest = new IntentCreateRequest(amount, order.getId());
        IntentCreateResponse paymentIntent = paymentService.createIntent(intentCreateRequest);

        return new OrderCreateResponse(order.getId(), result.booking(), paymentIntent);
    }

    private void deleteUncompletedOrders() {

    }
}
