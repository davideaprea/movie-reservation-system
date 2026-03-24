package com.mrs.app.hall.service;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallGetResponse;
import com.mrs.app.hall.dto.HallResponse;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.mapper.HallMapper;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.shared.exception.DomainRequirementError;
import com.mrs.app.shared.exception.DomainRequirementException;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
        List<HallCreateRequest.SeatDTO> seats = createRequest.seats();

        seats.sort(Comparator
                .comparingLong(HallCreateRequest.SeatDTO::rowNumber)
                .thenComparing(HallCreateRequest.SeatDTO::seatNumber));

        if (seats.getFirst().rowNumber() != 1) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "Rows should start from 1.",
                    "rowNumber"
            ));
        }

        for (int i = 1; i < seats.size(); i++) {
            HallCreateRequest.SeatDTO curr = seats.get(i);
            HallCreateRequest.SeatDTO prev = seats.get(i - 1);
            boolean sameRow = curr.rowNumber() == prev.rowNumber();
            boolean areSeatsNonAdjacent = sameRow && curr.seatNumber() != prev.seatNumber() + 1;
            boolean areRowsNonAdjacent = !sameRow && curr.rowNumber() != prev.rowNumber() + 1;
            boolean isCurrSeatNamedFirst = !sameRow && curr.seatNumber() != 1;

            if (areSeatsNonAdjacent || areRowsNonAdjacent || isCurrSeatNamedFirst) {
                throw new DomainRequirementException(new DomainRequirementError(
                        "Gaps are not allowed: %s, %s.".formatted(prev, curr),
                        "seatNumber"
                ));
            }
        }

        return hallMapper.toResponse(hallDAO.save(hallMapper.toEntity(createRequest)));
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
