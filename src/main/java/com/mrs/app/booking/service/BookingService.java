package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.mapper.BookingMapper;
import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.booking.repository.BookingDAO;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.service.ScheduleService;
import com.mrs.app.shared.exception.ConflictingEntityException;
import com.mrs.app.shared.exception.ConflictingResourceError;
import com.mrs.app.shared.exception.DomainRequirementError;
import com.mrs.app.shared.exception.DomainRequirementException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingDAO bookingDAO;
    private final BookingMapper bookingMapper;
    private final ScheduleService scheduleService;

    @Transactional
    public BookingResponse create(BookingCreateRequest createRequest) {
        ScheduleResponse selectedSchedule = scheduleService.findById(createRequest.scheduleId());

        if (LocalDateTime.now().isAfter(selectedSchedule.startTime())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected schedule is already over.",
                    BookingCreateRequest.Fields.scheduleId
            ));
        }

        Booking bookingToSave = Booking.builder().scheduleId(createRequest.scheduleId()).build();

        createRequest.scheduleSeatIds().forEach(seatId -> bookingToSave.addSeatReservation(SeatReservation.builder()
                .booking(bookingToSave)
                .scheduleSeatId(seatId)
                .build()));

        Booking savedBooking;

        try {
            savedBooking = bookingDAO.save(bookingToSave);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictingEntityException(new ConflictingResourceError<>(
                    List.of(),
                    List.of(BookingCreateRequest.Fields.scheduleSeatIds),
                    "These seats are already booked."
            ));
        }

        return bookingMapper.toResponse(savedBooking);
    }

    @Transactional
    public void deleteById(long id) {
        Booking bookingToDelete = bookingDAO.findById(id).orElseThrow();
        ScheduleResponse bookingSchedule = scheduleService.findById(bookingToDelete.getScheduleId());

        if (LocalDateTime.now().isAfter(bookingSchedule.startTime())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected schedule is already over.",
                    "scheduleId"
            ));
        }

        bookingDAO.deleteById(id);
    }
}
