package io.github.davideaprea.schedule.service;

import io.github.davideaprea.movie.dto.MovieResponse;
import io.github.davideaprea.hall.service.HallService;
import io.github.davideaprea.schedule.dao.ScheduleSpecificationBuilder;
import io.github.davideaprea.schedule.dto.ScheduleCreateRequest;
import io.github.davideaprea.movie.service.MovieService;
import io.github.davideaprea.schedule.dto.ScheduleResponse;
import io.github.davideaprea.schedule.dto.ScheduleGetRequestFilters;
import io.github.davideaprea.schedule.entity.Schedule;
import io.github.davideaprea.schedule.dao.ScheduleDAO;
import io.github.davideaprea.schedule.entity.ScheduleSeat;
import io.github.davideaprea.schedule.mapper.ScheduleMapper;
import io.github.davideaprea.shared.exception.ConflictingEntityException;
import io.github.davideaprea.shared.exception.ConflictingResourceError;
import io.github.davideaprea.shared.exception.EntityNotFondException;
import io.github.davideaprea.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleDAO scheduleDAO;
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
    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest dto) {
        MovieResponse movieToSchedule = movieService.findById(dto.movieId());
        LocalDateTime scheduleEndTime = dto.startTime().plus(movieToSchedule.duration());
        Schedule scheduleToSave = scheduleMapper.toEntity(dto, scheduleEndTime);
        List<ScheduleResponse> conflictingSchedules = findAllByFilters(new ScheduleGetRequestFilters(null, dto.startTime(), scheduleEndTime, dto.hallId()));

        if (!conflictingSchedules.isEmpty()) {
            ConflictingResourceError<ScheduleResponse> error = new ConflictingResourceError<>(
                    conflictingSchedules,
                    List.of(ScheduleCreateRequest.Fields.startTime, ScheduleCreateRequest.Fields.hallId),
                    "This hall is already taken."
            );

            throw new ConflictingEntityException(error);
        }

        hallService
                .findById(dto.hallId())
                .seats()
                .forEach(seat -> {
                    BigDecimal seatPrice = dto.seatPriceOptions().get(seat.type().name());

                    scheduleToSave.addSeat(ScheduleSeat.builder()
                            .price(seatPrice)
                            .seatId(seat.id())
                            .schedule(scheduleToSave)
                            .build());
                });

        return scheduleMapper.toResponse(scheduleDAO.save(scheduleToSave));
    }

    public ScheduleResponse findById(long id) {
        return scheduleDAO
                .findById(id)
                .map(scheduleMapper::toResponse)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Schedule.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }

    public List<ScheduleResponse> findAllByFilters(ScheduleGetRequestFilters filters) {
        return scheduleDAO
                .findAll(ScheduleSpecificationBuilder.fromFilters(filters))
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }
}
