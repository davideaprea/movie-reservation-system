package com.mrs.app.schedule.service;

import com.mrs.app.location.dto.HallGetResponse;
import com.mrs.app.location.dto.HallResponse;
import com.mrs.app.location.service.HallService;
import com.mrs.app.movie.dto.MovieResponse;
import com.mrs.app.movie.service.MovieService;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleGetRequestFilters;
import com.mrs.app.schedule.dto.ScheduleGetResponse;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.entity.ScheduleSeat;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import com.mrs.app.schedule.repository.ScheduleRepository;
import com.mrs.app.schedule.repository.ScheduleSpecificationBuilder;
import com.mrs.app.shared.exception.*;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final MovieService movieService;
    private final HallService hallService;

    /**
     * When creating a schedule, this method automatically calculates
     * the end time from the movie's duration and
     * generates a {@link ScheduleSeat} for each hall seat, setting its price
     * according to {@link ScheduleCreateRequest#seatPriceOptions()}.
     *
     * @throws ConflictingEntityException if the hall is already occupied during the requested time
     */
    @Observed(name = "schedule.create", contextualName = "Schedule creation")
    @Transactional
    public ScheduleResponse create(long operatorCinemaId, ScheduleCreateRequest dto) {
        log.info("Creating schedule with params {}", dto);

        MovieResponse movieToSchedule = movieService.findById(dto.movieId());
        LocalDateTime scheduleEndTime = dto.startTime().plus(movieToSchedule.duration());
        Schedule scheduleToSave = scheduleMapper.toEntity(dto, scheduleEndTime);
        List<ScheduleGetResponse> conflictingSchedules = findAllByFilters(new ScheduleGetRequestFilters(null, dto.startTime(), scheduleEndTime, dto.hallId()));

        if (!conflictingSchedules.isEmpty()) {
            log.warn("Couldn't save the schedule due to conflicting resources: {}", conflictingSchedules);

            ConflictingResourceError<ScheduleGetResponse> error = new ConflictingResourceError<>(
                    conflictingSchedules,
                    List.of(ScheduleCreateRequest.Fields.startTime, ScheduleCreateRequest.Fields.hallId),
                    "This hall is already taken."
            );

            throw new ConflictingEntityException(error);
        }

        HallResponse hall = hallService.findById(dto.hallId());

        if (hall.cinemaId() != operatorCinemaId) {
            throw new UnauthorizedOperationException("The selected hall is from a different cinema.");
        }

        hall.seats().forEach(seat -> {
            BigDecimal seatPrice = dto.seatPriceOptions().get(seat.type().name());

            scheduleToSave.addSeat(ScheduleSeat.builder()
                    .price(seatPrice)
                    .seatId(seat.id())
                    .schedule(scheduleToSave)
                    .build());
        });

        ScheduleResponse savedSchedule = scheduleMapper.toResponse(scheduleRepository.save(scheduleToSave));

        log.info("Schedule created with id {}.", savedSchedule.id());

        return savedSchedule;
    }

    public ScheduleResponse findById(long id) {
        return scheduleRepository
                .findById(id)
                .map(scheduleMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(new EntityNotFoundError(
                        Schedule.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }

    public List<ScheduleGetResponse> findAllByFilters(ScheduleGetRequestFilters filters) {
        List<Long> cinemaHallsIds = hallService.findAllByCinemaId(filters.cinemaId())
                .stream().map(HallGetResponse::id).toList();

        return scheduleRepository
                .findAll(new ScheduleSpecificationBuilder()
                        .movieId(filters.movieId())
                        .startTimeFrom(filters.startTimeFrom())
                        .endTimeTo(filters.endTimeTo())
                        .hallIdIn(cinemaHallsIds)
                        .build())
                .stream()
                .map(scheduleMapper::toGetResponse)
                .toList();
    }
}
