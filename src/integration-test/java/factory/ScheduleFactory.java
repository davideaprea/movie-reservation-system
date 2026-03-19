package factory;

import com.mrs.app.hall.entity.Seat;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.entity.ScheduleSeat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleFactory {
    private ScheduleFactory() {
    }

    public static Schedule create(long hallId, long movieId, List<Seat> seats) {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        Schedule schedule = Schedule.builder()
                .startTime(startTime)
                .endTime(startTime.plusHours(2))
                .movieId(movieId)
                .hallId(hallId)
                .build();

        seats.forEach(seat -> schedule.addSeat(ScheduleSeat.builder()
                .price(BigDecimal.valueOf(5))
                .schedule(schedule)
                .seatId(seat.getId())
                .build()));

        return schedule;
    }
}
