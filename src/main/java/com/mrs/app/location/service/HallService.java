package com.mrs.app.location.service;

import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.dto.HallGetResponse;
import com.mrs.app.location.dto.HallResponse;
import com.mrs.app.location.entity.Hall;
import com.mrs.app.location.entity.Seat;
import com.mrs.app.location.entity.SeatType;
import com.mrs.app.location.mapper.HallMapper;
import com.mrs.app.location.repository.HallRepository;
import com.mrs.app.shared.exception.EntityNotFoundException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class HallService {
    private final HallRepository hallRepository;
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

        return hallMapper.toResponse(hallRepository.save(hallToSave));
    }

    public HallResponse findById(long id) {
        return hallRepository
                .findById(id)
                .map(hallMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(new EntityNotFoundError(
                        Hall.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }

    public List<HallGetResponse> findAllByCinemaId(long cinemaId) {
        return hallRepository.findAllByCinemaId(cinemaId).stream()
                .map(hallMapper::toGetResponse)
                .toList();
    }
}
