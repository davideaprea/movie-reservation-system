package schedule;

import com.mrs.app.hall.entity.Hall;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.shared.exception.ConflictingResourceError;
import factory.TestDataFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Transactional
@SpringBootTest
@RequiredArgsConstructor
public class ScheduleControllerTest {
    private final RestTestClient restTestClient;
    private final TestDataFactory testDataFactory;

    private Movie movie;
    private Hall hall;

    @BeforeEach
    void setup() {
        movie = testDataFactory.createMovie();
        hall = testDataFactory.createHall();
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingSchedule_thenStatusCreated() {
        ScheduleCreateRequest request = new ScheduleCreateRequest(
                movie.getId(),
                hall.getId(),
                LocalDateTime.now().plusDays(1),
                Map.of("STANDARD", BigDecimal.valueOf(5))
        );

        restTestClient.post().body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(ScheduleResponse.class);
    }

    @SneakyThrows
    @Test
    void givenConflictingTimeRange_whenCreatingSchedule_thenStatusConflict() {
        Schedule schedule = testDataFactory.createSchedule(movie.getId(), hall.getSeats());
        ScheduleCreateRequest conflictingRequest = new ScheduleCreateRequest(
                schedule.getMovieId(),
                schedule.getHallId(),
                schedule.getStartTime(),
                Map.of("STANDARD", BigDecimal.valueOf(5))
        );

        restTestClient.post().body(conflictingRequest).exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ConflictingResourceError.class);
    }
}
