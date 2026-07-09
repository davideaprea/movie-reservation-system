package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.CinemaCreateRequest;
import com.mrs.app.location.dto.CinemaResponse;
import com.mrs.app.location.entity.Cinema;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaMapper {
    Cinema toEntity(CinemaCreateRequest request);

    CinemaResponse toResponse(Cinema cinema);
}
