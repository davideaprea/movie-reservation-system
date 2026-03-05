package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.BookingCreateResponse;
import com.mrs.app.booking.mapper.BookingMapper;
import com.mrs.app.location.dto.SeatGetResponse;
import com.mrs.app.location.service.SeatService;
import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.repository.BookingDAO;
import com.mrs.app.schedule.dto.ScheduleDTO;
import com.mrs.app.schedule.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDAO bookingDAO;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
    private final BookingMapper bookingMapper;

    public BookingCreateResponse create(BookingCreateRequest createRequest) {
        SeatGetResponse seat = seatService.findById(createRequest.seatId());
        ScheduleDTO schedule = scheduleService.findById(createRequest.scheduleId());

        if (seat.hallId() != schedule.hallId()) {
            //throw
        }

        Booking bookingToSave = bookingMapper.toEntity(createRequest);
        Booking savedBooking = bookingDAO.save(bookingToSave);

        return bookingMapper.toResponse(savedBooking);
    }
}
