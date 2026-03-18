package schedule;

import com.mrs.app.hall.entity.Hall;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.shared.exception.ConflictingResourceError;
import factory.TestDataFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
@AllArgsConstructor
public class ScheduleControllerTest {
    private final RestTestClient restTestClient;
    private final ScheduleDAO scheduleDAO;
    private final TestDataFactory testDataFactory;

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingSchedule_thenStatusCreated() {
        Movie movie = testDataFactory.createMovie();
        Hall hall = testDataFactory.createHall();
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
        LocalDateTime scheduleStartTime = LocalDateTime.now().plusDays(1);
        LocalDateTime scheduleEndTime = scheduleStartTime.plusHours(2);
        Schedule schedule = scheduleDAO.save(new Schedule(null, 1L, null, scheduleStartTime, scheduleEndTime));
        ScheduleCreateRequest conflictingRequest = new ScheduleCreateRequest(
                1,
                1,
                scheduleStartTime,
                Map.of("STANDARD", BigDecimal.valueOf(5))
        );

        restTestClient.post().body(conflictingRequest).exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ConflictingResourceError.class);
    }
}
