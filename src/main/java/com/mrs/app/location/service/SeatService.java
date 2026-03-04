package com.mrs.app.location.service;

import com.mrs.app.location.entity.Seat;
import com.mrs.app.schedule.dto.ScheduleSeatDetails;
import com.mrs.app.location.repository.SeatDAO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class SeatService {
    private final SeatDAO seatDao;

    public List<Seat> findAll(List<Long> seatIds) {
        List<Seat> seats = seatDao.findAllByIdIn(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found.");
        }

        seats.sort(Comparator.comparingInt(Seat::getSeatNumber));

        return seats;
    }

    public List<ScheduleSeatDetails> findScheduleSeats(long scheduleId) {
        List<ScheduleSeatDetails> scheduleSeats = seatDao.findScheduleSeats(scheduleId);

        if (scheduleSeats.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found.");
        }

        return scheduleSeats;
    }
}
