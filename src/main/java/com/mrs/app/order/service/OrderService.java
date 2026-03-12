package com.mrs.app.order.service;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingCreateResponse;
import com.mrs.app.booking.service.BookingService;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.OrderCreateRequest;
import com.mrs.app.order.dto.OrderCreateResponse;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.service.PaymentService;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.service.ScheduleSeatService;
import com.mrs.app.schedule.service.ScheduleService;
import com.mrs.app.shared.exception.DomainRequirementError;
import com.mrs.app.shared.exception.DomainRequirementException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final ScheduleService scheduleService;
    private final ScheduleSeatService scheduleSeatService;

    @Transactional
    public OrderCreateResponse create(OrderCreateRequest createRequest) {
        ScheduleResponse schedule = scheduleService.findById(createRequest.scheduleId());
        List<ScheduleSeatResponse> selectedSeats = scheduleSeatService.findAllByIds(createRequest.seatIds());
        boolean areSeatsFromADifferentSchedule = selectedSeats
                .stream()
                .anyMatch(seat -> seat.scheduleId() != createRequest.scheduleId());

        if (areSeatsFromADifferentSchedule) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "All seats must be from the same selected schedule.",
                    OrderCreateRequest.Fields.seatIds
            ));
        }

        if (LocalDateTime.now().isAfter(schedule.startTime())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected schedule is already over.",
                    OrderCreateRequest.Fields.scheduleId
            ));
        }

        BigDecimal totalPrice = selectedSeats.stream().map(ScheduleSeatResponse::price).reduce(BigDecimal.ZERO, BigDecimal::add);
        PaymentResponse payment = paymentService.create(new PaymentCreateRequest(createRequest.userId(), totalPrice));
        Order order = orderDAO.save(new Order(null, payment.id(), createRequest.userId(), LocalDateTime.now()));
        List<BookingCreateResponse> bookings = createRequest.seatIds().stream()
                .map(seatId -> bookingService.create(new BookingCreateRequest(seatId, createRequest.userId())))
                .toList();

        return new OrderCreateResponse(order.getId(), order.getCreatedAt(), bookings, payment);
    }
}
