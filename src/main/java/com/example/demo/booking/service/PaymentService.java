package com.example.demo.booking.service;

import com.example.demo.booking.dto.Amount;
import com.example.demo.booking.dto.BookingDto;
import com.example.demo.booking.dto.OrderDto;
import com.example.demo.booking.dto.PurchaseUnit;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.enumeration.PayPalOrderIntent;
import com.example.demo.booking.repository.PaymentDao;
import com.example.demo.booking.response.PayPalOrder;
import com.example.demo.cinema.projection.SeatDetail;
import com.example.demo.cinema.repository.SeatDao;
import com.example.demo.security.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PayPalService payPalService;
    private final BookingService bookingService;
    private final PaymentDao paymentDao;
    private final SeatDao seatDao;

    @Transactional
    public Payment create(BookingDto dto, long userId) {
        List<SeatDetail> seatDetails = getSelectedSeats(dto.seatIds());
        BigDecimal totalPrice = calculatePrice(seatDetails);

        Amount amount = new Amount("EUR", String.valueOf(totalPrice));

        OrderDto orderDto = new OrderDto(
                PayPalOrderIntent.CAPTURE,
                List.of(new PurchaseUnit(amount))
        );

        PayPalOrder order = payPalService.createOrder(orderDto);

        Payment payment = paymentDao.save(Payment.create(
                order.id(),
                totalPrice,
                User.create(userId)
        ));

        bookingService.create(seatDetails, dto.scheduleId(), payment.getId());

        return payment;
    }

    @Transactional
    public void confirm(String orderId, long userId) {
        if (!paymentDao.isPaymentUncaptured(orderId, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already captured.");
        }

        String captureId = payPalService.captureOrder(orderId);
        int updatedRows = paymentDao.confirm(orderId, captureId, userId);

        if (updatedRows != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found.");
        }
    }

    private List<SeatDetail> getSelectedSeats(List<Long> seatIds) {
        List<SeatDetail> selectedSeats = seatDao.findAll(seatIds);

        if (selectedSeats.size() != seatIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found.");
        }

        selectedSeats.sort(Comparator.comparingInt(SeatDetail::seatNumber));

        return selectedSeats;
    }

    private BigDecimal calculatePrice(List<SeatDetail> seatDetails) {
        return seatDetails
                .stream()
                .reduce(
                        BigDecimal.ZERO,
                        (sub, tot) -> sub.add(BigDecimal.valueOf(tot.seatType().getPrice())),
                        BigDecimal::add
                );
    }
}
