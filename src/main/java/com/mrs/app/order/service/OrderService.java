package com.mrs.app.order.service;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.service.BookingService;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.OrderCreateRequest;
import com.mrs.app.order.dto.OrderCreateResponse;
import com.mrs.app.order.dto.OrderUpdateRequest;
import com.mrs.app.order.dto.OrderUpdateResponse;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.service.PaymentService;
import com.mrs.app.schedule.dto.ScheduleResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public OrderCreateResponse create(OrderCreateRequest createRequest) {
        BigDecimal totalPrice = schedule.seats().stream()
                .map(ScheduleResponse.SeatDTO::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        PaymentResponse payment = paymentService.create(new PaymentCreateRequest(createRequest.userId(), totalPrice));
        Order order = orderDAO.save(new Order(null, payment.id(), createRequest.userId(), createRequest.scheduleId()));
        List<BookingResponse> bookings = createRequest.seatIds().stream()
                .map(seatId -> bookingService.create(new BookingCreateRequest(createRequest.scheduleId(), createRequest.seatIds())))
                .toList();

        return new OrderCreateResponse(order.getId(), bookings, payment);
    }

    public OrderUpdateResponse confirm(OrderUpdateRequest request) {
        Order order = orderDAO
                .findById(request.orderId())
                .filter(o -> o.getUserId() == request.userId())
                .orElseThrow();
        PaymentResponse payment = paymentService.complete(order.getPaymentId());

        return new OrderUpdateResponse(order.getId(), order.getUserId(), payment);
    }

    public OrderUpdateResponse cancel(OrderUpdateRequest request) {
        Order order = orderDAO
                .findById(request.orderId())
                .filter(o -> o.getUserId() == request.userId())
                .orElseThrow();

        bookingService.deleteById(order.getId());

        PaymentResponse payment = paymentService.refund(order.getPaymentId());

        return new OrderUpdateResponse(order.getId(), order.getUserId(), payment);
    }
}
