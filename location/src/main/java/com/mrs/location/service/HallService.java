package com.mrs.location.service;

import com.mrs.location.dto.HallCreateRequest;
import com.mrs.location.dto.HallGetResponse;
import com.mrs.location.dto.HallResponse;
import com.mrs.location.entity.Cinema;
import com.mrs.location.entity.Hall;
import com.mrs.location.entity.Seat;
import com.mrs.location.entity.SeatType;
import com.mrs.location.mapper.HallMapper;
import com.mrs.location.repository.HallRepository;
import com.mrs.shared.model.CurrentUserProvider;
import com.mrs.shared.model.LoggedUser;
import com.mrs.shared.model.Role;
import com.mrs.shared.exception.EntityNotFoundException;
import com.mrs.shared.exception.UnauthorizedOperationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class HallService {
    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final CurrentUserProvider currentUserProvider;

    /**
     * When creating a hall, seats are generated from the provided rows ({@link HallCreateRequest#seatRows()}),
     * forming a grid where each seat is assigned a progressive (rowNumber, seatNumber) starting from 1.
     */
    @Transactional
    public HallResponse create(HallCreateRequest createRequest) {
        currentUserProvider.get().ifPresent(loggedUser -> {
            if (Role.OPERATOR.equals(loggedUser.role()) && loggedUser.cinemaId() != createRequest.cinemaId()) {
                throw new UnauthorizedOperationException("You don't have any access to this cinema.");
            }
        });

        Hall hallToSave = Hall.builder()
                .cinema(Cinema.builder().id(createRequest.cinemaId()).build())
                .name(createRequest.name())
                .build();

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
                .orElseThrow(() -> new EntityNotFoundException(
                        Hall.class,
                        Map.of("id", id)
                ));
    }

    public List<HallGetResponse> findAllByCinemaId(long cinemaId) {
        currentUserProvider.get().ifPresent(loggedUser -> {
            if (Role.OPERATOR.equals(loggedUser.role()) && loggedUser.cinemaId() != cinemaId) {
                throw new UnauthorizedOperationException("You don't have any access to this cinema.");
            }
        });

        return hallRepository.findAllByCinemaId(cinemaId).stream()
                .map(hallMapper::toGetResponse)
                .toList();
    }
}
