package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.CityResponse;
import com.mrs.app.location.entity.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResponse toResponse(City city);
}
