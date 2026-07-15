package com.mrs.location.mapper;

import com.mrs.location.dto.CinemaCreateRequest;
import com.mrs.location.dto.CinemaResponse;
import com.mrs.location.entity.Cinema;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaMapper {
    Cinema toEntity(CinemaCreateRequest request);

    CinemaResponse toResponse(Cinema cinema);
}
