package com.example.demo.booking.validator;

import com.example.demo.cinema.projection.SeatProjection;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class SeatsValidator {
    public void checkHall(List<SeatProjection> selectedSeats, long hallId) {
        boolean areSeatsFromScheduleHall = selectedSeats
                .stream()
                .allMatch(seat -> seat.hallId() == hallId);

        if (!areSeatsFromScheduleHall) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seats must be in the schedule hall.");
        }
    }

    public void checkAdjacency(List<SeatProjection> selectedSeats) {
        for (int i = 1; i < selectedSeats.size(); i++) {
            SeatProjection curr = selectedSeats.get(i);
            SeatProjection prev = selectedSeats.get(i - 1);

            boolean areAdjacent = curr.seatNumber() - prev.seatNumber() == 1;
            boolean areOnTheSameRow = curr.rowNumber() == prev.rowNumber();

            if (!areAdjacent || !areOnTheSameRow) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Seats must be distinct and adjacent.");
            }
        }
    }
}
