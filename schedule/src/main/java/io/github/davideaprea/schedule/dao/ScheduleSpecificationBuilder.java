package io.github.davideaprea.schedule.dao;

import com.mrs.app.schedule.dto.ScheduleGetRequestFilters;
import com.mrs.app.schedule.entity.Schedule;
import org.springframework.data.jpa.domain.Specification;

public class ScheduleSpecificationBuilder {
    private ScheduleSpecificationBuilder() {
    }

    public static Specification<Schedule> fromFilters(ScheduleGetRequestFilters filters) {
        Specification<Schedule> finalSpecification = Specification.allOf();

        if (filters.movieId() != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.equal(root.get(Schedule.Fields.movieId), filters.movieId()));
        }

        if (filters.startTimeFrom() != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Schedule.Fields.startTime), filters.startTimeFrom()));
        }

        if (filters.endTimeTo() != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.lessThanOrEqualTo(root.get(Schedule.Fields.endTime), filters.endTimeTo()));
        }

        if (filters.hallId() != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.equal(root.get(Schedule.Fields.hallId), filters.hallId()));
        }

        return finalSpecification;
    }
}
