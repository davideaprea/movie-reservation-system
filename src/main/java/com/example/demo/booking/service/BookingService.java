package com.example.demo.booking.service;

import com.example.demo.booking.entity.Booking;
import com.example.demo.booking.repository.BookingDao;
import com.example.demo.booking.validator.SeatsValidator;
import com.example.demo.cinema.projection.BookingSchedule;
import com.example.demo.cinema.projection.SeatProjection;
import com.example.demo.cinema.service.ScheduleService;
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
    private final SeatsValidator seatsValidator;

    @Transactional
    public List<Booking> create(List<SeatProjection> selectedSeats, long scheduleId, long paymentId) {
        seatsValidator.checkAdjacency(selectedSeats);

        BookingSchedule schedule = scheduleService.findProjectionById(scheduleId, BookingSchedule.class);

        if (LocalDateTime.now().isAfter(schedule.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Schedule's already started.");
        }

        seatsValidator.checkHall(selectedSeats, schedule.getHall().getId());

        List<Booking> bookings = selectedSeats
                .stream()
                .map(seat -> Booking.create(
                        paymentId,
                        seat.id(),
                        scheduleId
                ))
                .toList();

        return StreamSupport
                .stream(bookingDao.saveAll(bookings).spliterator(), false)
                .toList();
    }
}
