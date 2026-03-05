package com.mrs.app.location.service;

import com.mrs.app.location.dto.SeatGetResponse;
import com.mrs.app.location.mapper.SeatMapper;
import com.mrs.app.location.repository.SeatDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeatService {
    private final SeatDAO seatDAO;
    private final SeatMapper seatMapper;

    public List<SeatGetResponse> findAllByHallId(long hallId) {
        return seatDAO
                .findAllByHallId(hallId)
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }
}
