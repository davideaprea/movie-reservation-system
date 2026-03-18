package factory;

import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.entity.Seat;
import com.mrs.app.hall.enumeration.HallStatus;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.MovieDAO;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;

@AllArgsConstructor
@Profile("test")
@Component
public class TestDataFactory {
    private final HallDAO hallDAO;
    private final MovieDAO movieDAO;

    public Hall createHall() {
        Hall hall = new Hall(null, HallStatus.AVAILABLE, null);

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                hall.addSeat(Seat.builder()
                        .hall(hall)
                        .seatNumber(j)
                        .rowNumber(i)
                        .build());
            }
        }

        return hallDAO.save(hall);
    }

    public Movie createMovie() {
        return movieDAO.save(Movie.builder()
                .cover("cover-link")
                .description("Description")
                .title("Title")
                .duration(Duration.ofHours(2))
                .build());
    }
}
