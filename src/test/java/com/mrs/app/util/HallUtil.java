package com.mrs.app.util;

import com.mrs.app.cinema.entity.Hall;
import com.mrs.app.cinema.entity.Seat;
import com.mrs.app.cinema.enumeration.SeatType;
import com.mrs.app.cinema.repository.HallDao;
import com.mrs.app.cinema.repository.SeatDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Component
public class HallUtil {
    private final HallDao hallDao;
    private final SeatDao seatDao;

    public Hall createFakeHall() {
        return hallDao.save(Hall.create());
    }

    public List<Seat> createSeats(int rowsNumber, int seatsPerRow, long hallId) {
        List<Seat> seats = new ArrayList<>();

        for (int row = 1; row <= rowsNumber; row++) {
            for (int seat = 1; seat <= seatsPerRow; seat++) {
                seats.add(Seat.create(
                        SeatType.REGULAR,
                        row,
                        seat,
                        hallId
                ));
            }
        }

        return StreamSupport
                .stream(seatDao.saveAll(seats).spliterator(), false)
                .toList();
    }
}
