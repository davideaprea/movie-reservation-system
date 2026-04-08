package io.github.davideaprea.booking.service;

import io.github.davideaprea.booking.dto.BookingResponse;
import io.github.davideaprea.booking.entity.Booking;
import io.github.davideaprea.booking.mapper.BookingMapper;
import io.github.davideaprea.booking.dto.BookingCreateRequest;
import io.github.davideaprea.booking.entity.SeatReservation;
import io.github.davideaprea.booking.repository.BookingDAO;
import io.github.davideaprea.schedule.dto.ScheduleResponse;
import io.github.davideaprea.schedule.service.ScheduleService;
import io.github.davideaprea.shared.exception.ConflictingEntityException;
import io.github.davideaprea.shared.exception.ConflictingResourceError;
import io.github.davideaprea.shared.exception.DomainRequirementError;
import io.github.davideaprea.shared.exception.DomainRequirementException;
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

    /**
     * This method ensures an "all-or-nothing" reservation: either all requested seats are booked,
     * or none are persisted if there is a conflict.
     *
     * @throws DomainRequirementException if the selected schedule has already started
     * @throws ConflictingEntityException if any of the requested seats are already booked
     */
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

    /**
     * Deletes a specific booking and every related seat reservation.
     *
     * @throws DomainRequirementException if the selected schedule has already started
     */
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
