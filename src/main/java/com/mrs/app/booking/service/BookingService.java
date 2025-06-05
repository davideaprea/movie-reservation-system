package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.internal.BookingDto;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.repository.BookingDao;
import com.mrs.app.booking.validator.BookingSeatsValidator;
import com.mrs.app.cinema.dto.projection.BookingSchedule;
import com.mrs.app.cinema.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDao bookingDao;
    private final ScheduleService scheduleService;
    private final BookingSeatsValidator bookingSeatsValidator;

    @Transactional
    public List<Booking> create(BookingDto dto) {
        BookingSchedule schedule = scheduleService.findBookingScheduleById(dto.scheduleId());

        validateBookingTime(schedule.startTime());

        bookingSeatsValidator.checkHall(dto.selectedSeats(), schedule.hallId());
        bookingSeatsValidator.checkAdjacency(dto.selectedSeats());

        List<Booking> bookings = buildBookings(dto);

        return saveBookings(bookings);
    }


    public List<Booking> buildBookings(BookingDto dto) {
        return dto.selectedSeats()
                .stream()
                .map(seat -> Booking.create(
                        dto.paymentId(),
                        seat.id(),
                        dto.scheduleId()
                ))
                .toList();
    }

    private void validateBookingTime(LocalDateTime scheduleStartTime) {
        if (LocalDateTime.now().isAfter(scheduleStartTime)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Schedule's already started.");
        }
    }

    private List<Booking> saveBookings(List<Booking> bookings) {
        return StreamSupport
                .stream(bookingDao.saveAll(bookings).spliterator(), false)
                .toList();
    }

    public void deletePaymentBookings(long paymentId) {
        bookingDao.deleteAllByPaymentId(paymentId);
    }
}
