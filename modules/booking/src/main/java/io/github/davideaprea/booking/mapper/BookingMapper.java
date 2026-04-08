package io.github.davideaprea.booking.mapper;

import io.github.davideaprea.booking.dto.BookingResponse;
import io.github.davideaprea.booking.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse toResponse(Booking booking);
}
