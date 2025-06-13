package com.mrs.app.booking.validator;

import com.mrs.app.cinema.entity.Seat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingValidator {
    public void checkBookingTime(LocalDateTime scheduleStartTime) {
        if (LocalDateTime.now().isAfter(scheduleStartTime)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Schedule's already started.");
        }
    }

    public void checkSeatsHall(List<Seat> selectedSeats, long hallId) {
        boolean areSeatsFromScheduleHall = selectedSeats
                .stream()
                .allMatch(seat -> seat.getHall().getId() == hallId);

        if (!areSeatsFromScheduleHall) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seats must be in the schedule hall.");
        }
    }

    public void checkSeatsAdjacency(List<Seat> selectedSeats) {
        for (int i = 1; i < selectedSeats.size(); i++) {
            Seat curr = selectedSeats.get(i);
            Seat prev = selectedSeats.get(i - 1);

            boolean areAdjacent = curr.getSeatNumber() - prev.getSeatNumber() == 1;
            boolean areOnTheSameRow = curr.getRowNumber() == prev.getRowNumber();

            if (!areAdjacent || !areOnTheSameRow) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seats must be distinct and adjacent.");
            }
        }
    }
}
