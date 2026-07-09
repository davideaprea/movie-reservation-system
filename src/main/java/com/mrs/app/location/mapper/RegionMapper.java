package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.RegionResponse;
import com.mrs.app.location.entity.Region;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    RegionResponse toResponse(Region region);
}
