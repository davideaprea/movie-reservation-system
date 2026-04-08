package io.github.davideaprea.order.service;

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
 * Orchestrator for the complete booking workflow.
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
     * <h3>Transactional guarantees</h3>
     * <ul>
     *     <li>The booking creation, payment intent creation, and order persistence are executed
     *     within a single transactional boundary.</li>
     *     <li>If any step fails, the transaction is rolled back to prevent partial state persistence.</li>
     * </ul>
     *
     * <h3>Workflow steps</h3>
     * <ol>
     *     <li>Create a booking for the requested schedule and seats</li>
     *     <li>Calculate the total amount based on seat pricing</li>
     *     <li>Create a payment intent for the calculated amount</li>
     *     <li>Persist the order with references to booking and payment intent</li>
     *     <li>Submit the payment intent (executed outside the transaction)</li>
     * </ol>
     *
     * <h3>Resilience & eventual consistency (SAGA)</h3>
     * <ul>
     *     <li>If booking or intent creation fails, the transaction is rolled back and no order is created.</li>
     *     <li>The payment submission is executed after transaction commit to avoid holding DB locks
     *     during external API calls.</li>
     *     <li>If the payment submission fails, the system relies on asynchronous recovery mechanisms
     *     (e.g. retries, webhook callbacks, or scheduled reconciliation).</li>
     * </ul>
     *
     * <h3>Idempotency considerations</h3>
     * <ul>
     *     <li>This operation is expected to be invoked in an idempotent context (e.g. protected by client or API layer).</li>
     *     <li>Duplicate requests should be handled upstream or via idempotency keys when interacting with the payment provider.</li>
     * </ul>
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

    /**
     * Periodically removes orders associated with expired or uncompleted payment intents.
     * <p>
     * This task is part of the system's SAGA-based recovery and resource management:
     * it ensures that stale orders do not block business operations and frees up reserved seats.
     */
    @Scheduled(fixedDelayString = "${app.order.cleanup-delay}")
    @Transactional
    protected void deleteUncompletedOrders() {
        List<String> expiredPaymentsIds = paymentService
                .findExpiredIntents()
                .stream()
                .map(IntentResponse::id)
                .toList();

        orderDAO.deleteAllByIntentIdIn(expiredPaymentsIds);
    }
}
