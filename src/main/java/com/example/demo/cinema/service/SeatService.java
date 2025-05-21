package com.example.demo.cinema.service;

import com.example.demo.cinema.projection.SeatDetail;
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

    public List<SeatDetail> findAll(List<Long> seatIds) {
        List<SeatDetail> selectedSeats = seatDao.findAll(seatIds);

        if (selectedSeats.size() != seatIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found.");
        }

        selectedSeats.sort(Comparator.comparingInt(SeatDetail::seatNumber));

        return selectedSeats;
    }
}
