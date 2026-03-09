package com.mrs.app.schedule.configuration;

import com.mrs.app.schedule.dto.SchedulesGetFilters;
import com.mrs.app.schedule.entity.Schedule;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ScheduleSpecificationBuilder {
    public Specification<Schedule> fromFilters(SchedulesGetFilters filters) {
        Specification<Schedule> finalSpecification = Specification.where(null);

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
            ) -> criteriaBuilder.greaterThan(root.get(Schedule.Fields.startTime), filters.startTimeFrom()));
        }

        if (filters.endTimeTo() != null) {
            finalSpecification = finalSpecification.and((
                    root,
                    query,
                    criteriaBuilder
            ) -> criteriaBuilder.lessThan(root.get(Schedule.Fields.endTime), filters.endTimeTo()));
        }

        return finalSpecification;
    }
}
