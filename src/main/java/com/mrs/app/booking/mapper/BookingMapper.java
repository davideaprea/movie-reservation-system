package com.mrs.app.booking.mapper;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingCreateResponse;
import com.mrs.app.booking.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toEntity(BookingCreateRequest createRequest);

    BookingCreateResponse toResponse(Booking booking);
}
