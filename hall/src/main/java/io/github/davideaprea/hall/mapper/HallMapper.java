package io.github.davideaprea.hall.mapper;

import com.mrs.app.hall.dto.HallGetResponse;
import com.mrs.app.hall.dto.HallResponse;
import com.mrs.app.hall.entity.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    HallResponse toResponse(Hall hall);

    HallGetResponse toGetResponse(Hall hall);
}
