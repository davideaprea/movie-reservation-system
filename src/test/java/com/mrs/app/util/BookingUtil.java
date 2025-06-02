package com.mrs.app.util;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.Payment;
import com.mrs.app.booking.repository.BookingDao;
import com.mrs.app.booking.repository.PaymentDao;
import com.mrs.app.security.entity.User;
import com.mrs.app.security.enumeration.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BookingUtil {
    private final BookingDao bookingDao;
    private final AuthUtil authUtil;
    private final PaymentDao paymentDao;

    public Payment createFakeBooking(long seatId, long scheduleId) {
        User user = authUtil.createFakeUser(Roles.USER);

        Payment payment = paymentDao.save(new Payment(
                null,
                "ORDER_ID",
                "CAPTURE_ID",
                BigDecimal.valueOf(20),
                User.createWithId(user.getId()),
                LocalDateTime.now(),
                null
        ));

        bookingDao.save(Booking.create(
                payment.getId(),
                seatId,
                scheduleId
        ));

        return payment;
    }
}
