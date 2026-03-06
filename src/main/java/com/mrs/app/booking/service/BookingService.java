package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.BookingCreateResponse;
import com.mrs.app.booking.mapper.BookingMapper;
import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.repository.BookingDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDAO bookingDAO;
    private final BookingMapper bookingMapper;

    public BookingCreateResponse create(BookingCreateRequest createRequest) {
        Booking bookingToSave = bookingMapper.toEntity(createRequest);
        Booking savedBooking = bookingDAO.save(bookingToSave);

        return bookingMapper.toResponse(savedBooking);
    }
}
