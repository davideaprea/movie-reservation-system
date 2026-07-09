package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.CinemaAddress;
import com.mrs.app.location.dto.CinemaCreateRequest;
import com.mrs.app.location.dto.CinemaResponse;
import com.mrs.app.location.entity.Cinema;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaMapper {
    Cinema toEntity(CinemaCreateRequest request);

    Cinema.Address toAddress(CinemaAddress address);

    CinemaResponse toResponse(Cinema cinema);
}
