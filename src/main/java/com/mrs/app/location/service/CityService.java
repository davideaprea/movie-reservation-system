package com.mrs.app.location.service;

import com.mrs.app.location.dto.CityResponse;
import com.mrs.app.location.mapper.CityMapper;
import com.mrs.app.location.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public Page<CityResponse> findAllByRegionId(Pageable pageable, long regionId) {
        return cityRepository.findAllByRegionId(pageable, regionId)
                .map(cityMapper::toResponse);
    }
}
