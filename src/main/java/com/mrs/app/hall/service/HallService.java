package com.mrs.app.hall.service;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallGetResponse;
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

@AllArgsConstructor
@Service
public class HallService {
    private final HallDAO hallDAO;
    private final HallMapper hallMapper;

    @Transactional
    public Hall create(HallCreateRequest createRequest) {
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

            if (sameRow && curr.seatNumber() != prev.seatNumber() + 1) {
                throw new DomainRequirementException(new DomainRequirementError(
                        "Gap between seat %d and %d at row n. %d.".formatted(prev.seatNumber(), curr.seatNumber(), curr.rowNumber()),
                        "seatNumber"
                ));
            }

            if (!sameRow && (curr.rowNumber() != prev.rowNumber() + 1 || curr.seatNumber() != 1)) {
                throw new DomainRequirementException(new DomainRequirementError(
                        "Gap between row %d and %d.".formatted(prev.rowNumber(), prev.rowNumber()),
                        "seatNumber"
                ));
            }
        }

        return hallDAO.save(hallMapper.toEntity(createRequest));
    }

    public HallGetResponse findById(long id) {
        return hallDAO
                .findById(id)
                .map(hallMapper::toResponse)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Hall.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }
}
