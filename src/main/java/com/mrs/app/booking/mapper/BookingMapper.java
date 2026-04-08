package com.mrs.app.booking.mapper;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.dto.SeatReservationResponse;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse toResponse(Booking booking);

    SeatReservationResponse toResponse(SeatReservation seatReservation);
}
