package com.mrs.app.schedule.dao;

import com.mrs.app.schedule.entity.Schedule;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ScheduleSpecificationBuilder {
    private Specification<Schedule> specification;

    public ScheduleSpecificationBuilder() {
        specification = Specification.allOf();
    }

    public ScheduleSpecificationBuilder startTimeFrom(LocalDateTime startTimeFrom) {
        if (startTimeFrom != null) {
            specification = specification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.greaterThan(root.get(Schedule.Fields.startTime), startTimeFrom));
        }

        return this;
    }

    public ScheduleSpecificationBuilder endTimeTo(LocalDateTime endTimeTo) {
        if (endTimeTo != null) {
            specification = specification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.lessThan(root.get(Schedule.Fields.endTime), endTimeTo));
        }

        return this;
    }

    public ScheduleSpecificationBuilder movieId(Long movieId) {
        if (movieId != null) {
            specification = specification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.lessThan(root.get(Schedule.Fields.endTime), movieId));
        }

        return this;
    }

    public Specification<Schedule> build() {
        return specification;
    }
}
