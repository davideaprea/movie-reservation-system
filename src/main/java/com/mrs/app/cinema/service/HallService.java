package com.mrs.app.cinema.service;

import com.mrs.app.cinema.dto.request.HallDto;
import com.mrs.app.cinema.entity.Hall;
import com.mrs.app.cinema.repository.HallDao;
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
