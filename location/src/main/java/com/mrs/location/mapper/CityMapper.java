package com.mrs.location.mapper;

import com.mrs.location.dto.CityResponse;
import com.mrs.location.entity.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResponse toResponse(City city);
}
