package com.mrs.app.util;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.enumeration.PaymentStatus;
import com.mrs.app.booking.repository.BookingDAO;
import com.mrs.app.payment.repository.PaymentDao;
import com.mrs.app.security.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingUtil {
    private final BookingDAO bookingDao;
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
