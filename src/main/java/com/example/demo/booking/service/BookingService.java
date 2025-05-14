package com.example.demo.booking.service;

import com.example.demo.booking.dto.BookingDto;
import com.example.demo.booking.entity.Booking;
import com.example.demo.booking.repository.BookingDao;
import com.example.demo.cinema.projection.BookingSchedule;
import com.example.demo.cinema.projection.SeatDetail;
import com.example.demo.cinema.repository.ScheduleDao;
import com.example.demo.cinema.repository.SeatDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDao bookingDao;
    private final SeatDao seatDao;
    private final ScheduleDao scheduleDao;

    @Transactional
    public List<Booking> create(BookingDto dto, long userId, long scheduleId) {
        List<SeatDetail> selectedSeats = getSelectedSeats(dto.seatIds());

        checkRow(selectedSeats);
        checkSeatsAdjacency(selectedSeats);

        BookingSchedule schedule = getScheduleById(scheduleId);

        if(LocalDateTime.now().isAfter(schedule.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Schedule's already started.");
        }

        checkSeatHall(selectedSeats, schedule.getHall().getId());

        List<Booking> bookings = selectedSeats
                .stream()
                .map(seat -> Booking.create(
                        userId,
                        seat.id(),
                        scheduleId
                ))
                .toList();

        return StreamSupport
                .stream(bookingDao.saveAll(bookings).spliterator(), false)
                .toList();
    }

    private void checkSeatsAdjacency(List<SeatDetail> selectedSeats) {
        for (int i = 1; i < selectedSeats.size(); i++) {
            int curr = selectedSeats.get(i).seatNumber();
            int prev = selectedSeats.get(i - 1).seatNumber();

            if (curr - prev != 1) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seats must be distinct and adjacent.");
            }
        }
    }

    private void checkRow(List<SeatDetail> selectedSeats) {
        boolean areSeatsOnTheSameRow = selectedSeats
                .stream()
                .allMatch(seat -> seat.rowNumber() == selectedSeats.getFirst().rowNumber());

        if (!areSeatsOnTheSameRow) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seats must be on the same row.");
        }
    }

    private void checkSeatHall(List<SeatDetail> selectedSeats, long hallId) {
        boolean areSeatsFromScheduleHall = selectedSeats
                .stream()
                .allMatch(seat -> seat.hallId() == hallId);

        if (!areSeatsFromScheduleHall) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seats must be in the schedule hall.");
        }
    }

    private BookingSchedule getScheduleById(long id) {
        return scheduleDao
                .getProjectionById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found."));
    }

    private List<SeatDetail> getSelectedSeats(List<Long> seatIds) {
        List<SeatDetail> selectedSeats = seatDao.findAll(seatIds);

        if (selectedSeats.size() != seatIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found.");
        }

        selectedSeats.sort(Comparator.comparingInt(SeatDetail::seatNumber));

        return selectedSeats;
    }
}
