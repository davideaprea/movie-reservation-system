package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.internal.BookingDto;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.repository.BookingDao;
import com.mrs.app.booking.validator.BookingValidator;
import com.mrs.app.cinema.entity.Schedule;
import com.mrs.app.cinema.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDao bookingDao;
    private final ScheduleService scheduleService;
    private final BookingValidator bookingValidator;

    @Transactional
    public List<Booking> create(BookingDto dto) {
        Schedule schedule = scheduleService.findBookingScheduleById(dto.scheduleId());

        bookingValidator.checkBookingTime(schedule.getStartTime());
        bookingValidator.checkSeatsHall(dto.selectedSeats(), schedule.getHall().getId());
        bookingValidator.checkSeatsAdjacency(dto.selectedSeats());

        List<Booking> bookings = buildBookings(dto);

        return saveBookings(bookings);
    }

    private List<Booking> buildBookings(BookingDto dto) {
        return dto.selectedSeats()
                .stream()
                .map(seat -> Booking.create(
                        dto.paymentId(),
                        seat.getId(),
                        dto.scheduleId()
                ))
                .toList();
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
