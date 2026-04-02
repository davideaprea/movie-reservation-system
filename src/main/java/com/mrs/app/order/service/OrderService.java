package com.mrs.app.order.service;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.service.BookingService;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.*;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.dto.IntentCreateRequest;
import com.mrs.app.payment.dto.IntentResponse;
import com.mrs.app.payment.dto.IntentSubmissionRequest;
import com.mrs.app.payment.dto.IntentSubmissionResponse;
import com.mrs.app.payment.service.PaymentService;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.service.ScheduleSeatService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

            BigDecimal amount = scheduleSeatService.findAllByIdIn(createRequest.seatIds())
                    .stream()
                    .map(ScheduleSeatResponse::price)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            IntentResponse intent = paymentService.createIntent(new IntentCreateRequest(amount));

            Order order = orderDAO.save(Order.builder()
                    .createdAt(LocalDateTime.now())
                    .userId(createRequest.userId())
                    .bookingId(booking.id())
                    .intentId(intent.id())
                    .build());

            return new BookingTransactionResult(order, booking, intent);
        });
        IntentSubmissionRequest request = new IntentSubmissionRequest(result.intent().id());
        IntentSubmissionResponse submittedIntent = paymentService.submitIntent(request);

        return new OrderCreateResponse(
                result.order().getId(),
                result.booking(),
                result.intent(),
                submittedIntent
        );
    }

    @Scheduled(fixedDelayString = "${app.payment.cleaner.delay}")
    @Transactional
    protected void deleteUncompletedOrders() {
        List<String> expiredPaymentsIds = paymentService
                .findAllExpired()
                .stream()
                .map(IntentResponse::id)
                .toList();

        orderDAO.deleteAllByIntentIdIn(expiredPaymentsIds);
    }
}
