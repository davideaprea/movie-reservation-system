package com.mrs.app.util;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.Payment;
import com.mrs.app.booking.enumeration.PaymentStatus;
import com.mrs.app.booking.repository.BookingDao;
import com.mrs.app.booking.repository.PaymentDao;
import com.mrs.app.security.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingUtil {
    private final BookingDao bookingDao;
    private final PaymentDao paymentDao;

    public Payment createFakeBooking(long seatId, long scheduleId, long userId) {
        Payment payment = paymentDao.save(new Payment(
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(20),
                User.createWithId(userId),
                LocalDateTime.now(),
                null,
                PaymentStatus.COMPLETED
        ));

        bookingDao.save(Booking.create(
                payment.getId(),
                seatId,
                scheduleId
        ));

        return payment;
    }
}
