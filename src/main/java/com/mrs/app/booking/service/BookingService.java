package com.mrs.app.booking.service;

import com.mrs.app.location.dto.SeatGetResponse;
import com.mrs.app.location.service.SeatService;
import com.mrs.app.payment.constant.PaymentTimeouts;
import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.repository.BookingDAO;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDAO bookingDAO;
    private final ScheduleService scheduleService;
    private final SeatService seatService;

    @Transactional
    public List<Booking> create(BookingCreateRequest dto) {
        List<SeatGetResponse> seats = seatService.findRowSeats(
                scheduleService.findById(dto.scheduleId()).hallId(),
                dto.rowNumber(),
                dto.seatNumbers()
        );

        if (seats.size() != dto.seatNumbers().size()) {
            //throw
        }


    }

    public void deletePaymentBookings(long paymentId) {
        Schedule paymentSchedule = scheduleService.findPaymentSchedule(paymentId);

        checkRefundTimeWindow(paymentSchedule.getStartTime());

        bookingDAO.deleteAllByPaymentId(paymentId);
    }

    private void checkRefundTimeWindow(LocalDateTime scheduleStartTime) {
        final LocalDateTime now = LocalDateTime.now();

        Duration hoursDiff = Duration.between(now, scheduleStartTime);

        if (
                scheduleStartTime.isAfter(now) ||
                        hoursDiff.toHours() >= PaymentTimeouts.REFUND_ELIGIBILITY_WINDOW_HOURS
        ) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Refunds can be requested within 3 hours of the start of the schedule."
            );
        }
    }
}
