package com.example.demo.cinema.service;

import com.example.demo.cinema.dto.internal.SeatDto;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.cinema.dto.projection.ScheduleSeatDetails;
import com.example.demo.cinema.dto.projection.SeatProjection;
import com.example.demo.cinema.repository.SeatDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class SeatService {
    private final SeatDao seatDao;

    public List<SeatProjection> findAll(List<Long> seatIds) {
        List<SeatProjection> seats = seatDao.findAll(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found.");
        }

        seats.sort(Comparator.comparingInt(SeatProjection::seatNumber));

        return seats;
    }

    public List<ScheduleSeatDetails> findScheduleSeats(long scheduleId) {
        List<ScheduleSeatDetails> scheduleSeats = seatDao.findScheduleSeats(scheduleId);

        if (scheduleSeats.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found.");
        }

        return scheduleSeats;
    }

    @Transactional
    public List<Seat> createHallSeats(long hallId, List<SeatDto> seatDtos) {
        List<Seat> seats = seatDtos
                .stream()
                .map(dto -> Seat.create(
                        dto.type(),
                        dto.rowNumber(),
                        dto.seatNumber(),
                        hallId
                ))
                .toList();

        return StreamSupport
                .stream(seatDao.saveAll(seats).spliterator(), false)
                .toList();
    }
}
