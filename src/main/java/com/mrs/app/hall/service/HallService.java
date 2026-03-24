package com.mrs.app.hall.service;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallGetResponse;
import com.mrs.app.hall.dto.HallResponse;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.entity.Seat;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.mapper.HallMapper;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class HallService {
    private final HallDAO hallDAO;
    private final HallMapper hallMapper;

    @Transactional
    public HallResponse create(HallCreateRequest createRequest) {
        Hall hallToSave = new Hall(null, createRequest.name(), new ArrayList<>());

        for (int rowNumber = 0; rowNumber < createRequest.seatRows().size(); rowNumber++) {
            List<HallCreateRequest.SeatCreateRequest> row = createRequest.seatRows().get(rowNumber);

            for (int seatNumber = 0; seatNumber < row.size(); seatNumber++) {
                HallCreateRequest.SeatCreateRequest seat = row.get(seatNumber);

                hallToSave.addSeat(Seat.builder()
                        .rowNumber(rowNumber + 1)
                        .seatNumber(seatNumber + 1)
                        .hall(hallToSave)
                        .type(new SeatType(seat.seatTypeId(), null))
                        .build());
            }
        }

        return hallMapper.toResponse(hallDAO.save(hallToSave));
    }

    public HallResponse findById(long id) {
        return hallDAO
                .findById(id)
                .map(hallMapper::toResponse)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Hall.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }

    public List<HallGetResponse> findAll() {
        return StreamSupport.stream(hallDAO.findAll().spliterator(), false)
                .map(hallMapper::toGetResponse)
                .toList();
    }
}
