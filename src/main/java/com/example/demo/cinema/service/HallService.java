package com.example.demo.cinema.service;

import com.example.demo.cinema.dto.HallDto;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.repository.HallDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class HallService {
    private final HallDao hallDao;
    private final SeatService seatService;

    @Transactional
    public Hall create(HallDto dto) {
        Hall hall = hallDao.save(Hall.create());

        seatService.createHallSeats(hall.getId(), dto.seatDtos());

        return hall;
    }
}
