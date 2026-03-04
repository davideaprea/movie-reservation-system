package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.entity.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    Hall toEntity(HallCreateRequest createRequest);
}
