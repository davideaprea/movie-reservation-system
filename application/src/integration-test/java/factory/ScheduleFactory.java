package factory;

import com.mrs.location.entity.Hall;
import com.mrs.movie.entity.Movie;
import com.mrs.schedule.entity.Schedule;
import com.mrs.schedule.entity.ScheduleSeat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleFactory {
    public static Schedule create(Hall hall, Movie movie) {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        Schedule schedule = Schedule.builder()
                .startTime(startTime)
                .endTime(startTime.plus(movie.getDuration()))
                .movieId(movie.getId())
                .hallId(hall.getId())
                .build();

        hall.getSeats().forEach(seat -> schedule.addSeat(ScheduleSeat.builder()
                .price(BigDecimal.valueOf(5))
                .schedule(schedule)
                .seatId(seat.getId())
                .build()));

        return schedule;
    }
}
