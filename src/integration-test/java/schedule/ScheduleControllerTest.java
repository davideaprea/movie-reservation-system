package schedule;

import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.MovieDAO;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import com.mrs.app.shared.exception.ConflictingResourceError;
import config.TestContainersConfiguration;
import factory.HallFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.transaction.annotation.Transactional;
import factory.MovieFactory;
import factory.ScheduleFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfiguration.class)
@Transactional
@RequiredArgsConstructor
public class ScheduleControllerTest {
    private final RestTestClient restTestClient;
    private final ScheduleDAO scheduleDAO;
    private final MovieDAO movieDAO;
    private final HallDAO hallDAO;
    private final ScheduleMapper scheduleMapper;

    private Movie movie;
    private Hall hall;

    @BeforeEach
    void setup() {
        movie = movieDAO.save(MovieFactory.create());
        hall = hallDAO.save(HallFactory.create());
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingSchedule_thenStatusCreated() {
        BigDecimal seatPrice = BigDecimal.valueOf(5);
        ScheduleCreateRequest request = new ScheduleCreateRequest(
                movie.getId(),
                hall.getId(),
                LocalDateTime.now().plusDays(1),
                Map.of("STANDARD", seatPrice)
        );
        ScheduleResponse actualResponse = restTestClient.post().body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(ScheduleResponse.class)
                .returnResult().getResponseBody();
        ScheduleResponse expectedResponse = new ScheduleResponse(
                actualResponse.id(),
                request.movieId(),
                request.hallId(),
                request.startTime(),
                request.startTime().plus(movie.getDuration()),
                hall.getSeats()
                        .stream()
                        .map(seat -> new ScheduleSeatResponse(1, seat.getId(), seatPrice))
                        .toList()
        );

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("seats", "id")
                .isEqualTo(expectedResponse);

        assertThat(actualResponse.seats())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrderElementsOf(expectedResponse.seats());

        assertThat(scheduleDAO.count()).isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void givenConflictingTimeRange_whenCreatingSchedule_thenStatusConflict() {
        Schedule preExistingSchedule = scheduleDAO.save(ScheduleFactory.create(hall.getId(), movie.getId(), hall.getSeats()));
        ScheduleCreateRequest conflictingRequest = new ScheduleCreateRequest(
                preExistingSchedule.getMovieId(),
                preExistingSchedule.getHallId(),
                preExistingSchedule.getStartTime(),
                Map.of("STANDARD", BigDecimal.valueOf(5))
        );
        ConflictingResourceError response = restTestClient.post().body(conflictingRequest).exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ConflictingResourceError.class)
                .returnResult().getResponseBody();

        assertThat(scheduleDAO.count()).isEqualTo(1);
        assertTrue(scheduleDAO.existsById(preExistingSchedule.getId()));
        assertThat(response).isEqualTo(new ConflictingResourceError(
                List.of(scheduleMapper.toDTO(preExistingSchedule)),
                List.of(ScheduleCreateRequest.Fields.startTime, ScheduleCreateRequest.Fields.hallId),
                "This hall is already taken."
        ));
    }
}
