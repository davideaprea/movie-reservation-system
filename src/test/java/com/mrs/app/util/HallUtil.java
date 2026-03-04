package com.mrs.app.util;

import com.mrs.app.location.entity.Hall;
import com.mrs.app.location.entity.Seat;
import com.mrs.app.location.repository.HallDAO;
import com.mrs.app.location.repository.SeatDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Component
public class HallUtil {
    private final HallDAO hallDao;
    private final SeatDAO seatDao;

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
