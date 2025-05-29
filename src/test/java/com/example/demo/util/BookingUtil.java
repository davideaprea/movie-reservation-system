package com.example.demo.util;

import com.example.demo.booking.entity.Booking;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.repository.BookingDao;
import com.example.demo.booking.repository.PaymentDao;
import com.example.demo.security.entity.User;
import com.example.demo.security.enumeration.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
                User.create(user.getId()),
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
