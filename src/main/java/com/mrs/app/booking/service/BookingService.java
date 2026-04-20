package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.booking.dto.SeatReservationResponse;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.booking.mapper.BookingMapper;
import com.mrs.app.booking.repository.BookingRepository;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.service.ScheduleService;
import com.mrs.app.shared.exception.ConflictingEntityException;
import com.mrs.app.shared.exception.ConflictingResourceError;
import com.mrs.app.shared.exception.DomainRequirementError;
import com.mrs.app.shared.exception.DomainRequirementException;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@AllArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ScheduleService scheduleService;

    /**
     * This method ensures an "all-or-nothing" reservation: either all requested seats are booked,
     * or none are persisted if there is a conflict.
     *
     * @throws DomainRequirementException if the selected schedule has already started
     * @throws ConflictingEntityException if any of the requested seats are already booked
     */
    @Observed(name = "booking.create", contextualName = "Booking creation")
    @Transactional
    public BookingResponse create(BookingCreateRequest createRequest) {
        log.info("Creating booking with params: {}", createRequest);

        ScheduleResponse selectedSchedule = scheduleService.findById(createRequest.scheduleId());
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(selectedSchedule.startTime())) {
            log.warn("""
                    Couldn't create any booking because the selected schedule is already over.
                    Selected schedule start time: {}. Submission time: {}.
                    """, selectedSchedule.startTime(), now);

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
            savedBooking = bookingRepository.save(bookingToSave);
        } catch (DataIntegrityViolationException e) {
            log.warn("The selected seats {} are already booked.", createRequest.scheduleSeatIds());

            throw new ConflictingEntityException(new ConflictingResourceError<>(
                    List.of(),
                    List.of(BookingCreateRequest.Fields.scheduleSeatIds),
                    "These seats are already booked."
            ));
        }

        log.info("Created booking with id {}.", savedBooking.getId());

        return bookingMapper.toResponse(savedBooking);
    }

    /**
     * Deletes a specific booking and every related seat reservation.
     *
     * @throws DomainRequirementException if the selected schedule has already started
     */
    @Transactional
    public void deleteById(long id) {
        Booking bookingToDelete = bookingRepository.findById(id).orElseThrow();
        ScheduleResponse bookingSchedule = scheduleService.findById(bookingToDelete.getScheduleId());

        if (LocalDateTime.now().isAfter(bookingSchedule.startTime())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected schedule is already over.",
                    "scheduleId"
            ));
        }

        bookingRepository.deleteById(id);
    }

    public List<SeatReservationResponse> findSeatReservationsByScheduleId(long scheduleId) {
        return bookingRepository
                .findAllByScheduleId(scheduleId)
                .stream().map(bookingMapper::toResponse)
                .toList();
    }

    public List<BookingResponse> findAllById(List<Long> bookingIds) {
        return StreamSupport.stream(
                bookingRepository.findAllById(bookingIds).spliterator(),
                false
        ).map(bookingMapper::toResponse).toList();
    }
}
