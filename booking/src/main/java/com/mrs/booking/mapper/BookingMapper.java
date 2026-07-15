package com.mrs.booking.mapper;

import com.mrs.booking.dto.BookingResponse;
import com.mrs.booking.dto.SeatReservationResponse;
import com.mrs.booking.entity.Booking;
import com.mrs.booking.entity.SeatReservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse toResponse(Booking booking);

    SeatReservationResponse toResponse(SeatReservation seatReservation);
}
