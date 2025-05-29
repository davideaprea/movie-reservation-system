package com.example.demo.booking.service;

import com.example.demo.booking.dto.BookingDto;
import com.example.demo.booking.entity.Booking;
import com.example.demo.booking.repository.BookingDao;
import com.example.demo.booking.validator.SeatsValidator;
import com.example.demo.cinema.projection.BookingSchedule;
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
    public List<Booking> create(BookingDto dto) {
        BookingSchedule schedule = scheduleService.findProjectionById(dto.scheduleId(), BookingSchedule.class);

        validateBookingTime(schedule.getStartTime());

        seatsValidator.checkHall(dto.selectedSeats(), schedule.getHall().getId());
        seatsValidator.checkAdjacency(dto.selectedSeats());

        List<Booking> bookings = buildBookings(dto);

        return saveBookings(bookings);
    }

    private void validateBookingTime(LocalDateTime scheduleStartTime) {
        if (LocalDateTime.now().isAfter(scheduleStartTime)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Schedule's already started.");
        }
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

    private List<Booking> saveBookings(List<Booking> bookings) {
        return StreamSupport
                .stream(bookingDao.saveAll(bookings).spliterator(), false)
                .toList();
    }
}
