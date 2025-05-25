package com.example.demo.cinema.service;

import com.example.demo.cinema.projection.SeatProjection;
import com.example.demo.cinema.repository.SeatDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

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
}
