package io.github.davideaprea.hall.service;

import io.github.davideaprea.hall.dto.HallCreateRequest;
import io.github.davideaprea.hall.dto.HallGetResponse;
import io.github.davideaprea.hall.dto.HallResponse;
import io.github.davideaprea.hall.entity.Hall;
import io.github.davideaprea.hall.entity.Seat;
import io.github.davideaprea.hall.entity.SeatType;
import io.github.davideaprea.hall.mapper.HallMapper;
import io.github.davideaprea.hall.repository.HallDAO;
import io.github.davideaprea.shared.exception.EntityNotFondException;
import io.github.davideaprea.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class HallService {
    private final HallDAO hallDAO;
    private final HallMapper hallMapper;

    /**
     * When creating a hall, seats are generated from the provided rows ({@link HallCreateRequest#seatRows()}),
     * forming a grid where each seat is assigned a progressive (rowNumber, seatNumber) starting from 1.
     */
    @Transactional
    public HallResponse create(HallCreateRequest createRequest) {
        Hall hallToSave = Hall.builder().name(createRequest.name()).build();

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
