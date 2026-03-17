package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.mapper.BookingMapper;
import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.booking.repository.BookingDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDAO bookingDAO;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponse create(BookingCreateRequest createRequest) {
        Booking bookingToSave = new Booking(null, new ArrayList<>(), createRequest.scheduleId());

        createRequest.scheduleSeatIds().forEach(seatId -> bookingToSave.addSeatReservation(new SeatReservation(null, seatId, bookingToSave)));

        Booking savedBooking = bookingDAO.save(bookingToSave);

        return bookingMapper.toResponse(savedBooking);
    }
}
