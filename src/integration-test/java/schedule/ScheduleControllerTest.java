package schedule;

import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.shared.exception.ConflictingResourceError;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
@AllArgsConstructor
public class ScheduleControllerTest {
    private final RestTestClient restTestClient;
    private final ScheduleDAO scheduleDAO;

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingSchedule_thenStatusCreated() {
        ScheduleCreateRequest request = new ScheduleCreateRequest(
                1,
                1,
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
