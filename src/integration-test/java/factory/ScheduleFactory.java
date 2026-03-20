package factory;

import com.mrs.app.hall.entity.Hall;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.entity.ScheduleSeat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ScheduleFactory {
    private ScheduleFactory() {
    }

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
