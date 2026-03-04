package com.mrs.app.location.service;

import com.mrs.app.location.dto.HallDto;
import com.mrs.app.location.entity.Hall;
import com.mrs.app.location.repository.HallDao;
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
