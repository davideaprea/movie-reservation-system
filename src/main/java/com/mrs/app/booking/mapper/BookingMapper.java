package com.mrs.app.booking.mapper;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse toResponse(Booking booking);
}
