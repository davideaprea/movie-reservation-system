package com.mrs.location.service;

import com.mrs.location.dto.CinemaCreateRequest;
import com.mrs.location.dto.CinemaResponse;
import com.mrs.location.entity.Cinema;
import com.mrs.location.mapper.CinemaMapper;
import com.mrs.location.repository.CinemaRepository;
import com.mrs.shared.exception.ConflictingEntityException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    public CinemaResponse create(CinemaCreateRequest request) {
        Cinema cinema = cinemaMapper.toEntity(request);

        try {
            cinema = cinemaRepository.save(cinema);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictingEntityException(
                    List.of(CinemaCreateRequest.Fields.addressId),
                    "This address is already taken."
            );
        }

        return cinemaMapper.toResponse(cinema);
    }

    public Page<CinemaResponse> findAllByCityId(Pageable pageable, long cityId) {
        return cinemaRepository.findAllByCityId(pageable, cityId).map(cinemaMapper::toResponse);
    }
}
