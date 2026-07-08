package com.mrs.app.schedule.repository;

import com.mrs.app.schedule.entity.Schedule;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleSpecificationBuilder {
    private Specification<Schedule> finalSpecification = Specification.allOf();

    public ScheduleSpecificationBuilder movieId(Long movieId) {
        if (movieId != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.equal(root.get(Schedule.Fields.movieId), movieId));
        }

        return this;
    }

    public ScheduleSpecificationBuilder startTimeFrom(LocalDateTime startTimeFrom) {
        if (startTimeFrom != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Schedule.Fields.startTime), startTimeFrom));
        }

        return this;
    }

    public ScheduleSpecificationBuilder endTimeTo(LocalDateTime endTimeTo) {
        if (endTimeTo != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.lessThanOrEqualTo(root.get(Schedule.Fields.endTime), endTimeTo));
        }

        return this;
    }

    public ScheduleSpecificationBuilder hallIdIn(List<Long> hallIds) {
        if (hallIds != null && !hallIds.isEmpty()) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> root.get(Schedule.Fields.hallId).in(hallIds));
        }

        return this;
    }

    public Specification<Schedule> build() {
        return finalSpecification;
    }
}
