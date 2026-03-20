package schedule;

import com.mrs.app.MRSApplication;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.hall.repository.SeatTypeDAO;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.MovieDAO;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.dao.UserDAO;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
import com.mrs.app.shared.exception.ConflictingResourceError;
import config.DataBaseCleaner;
import config.TestContainersConfiguration;
import factory.HallFactory;
import factory.UserFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.client.RestTestClient;
import factory.MovieFactory;
import factory.ScheduleFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@TestExecutionListeners(
        value = DataBaseCleaner.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@Import(TestContainersConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MRSApplication.class
)
public class ScheduleControllerTest {
    private RestTestClient restTestClient;
    @Autowired
    private ScheduleDAO scheduleDAO;
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private HallDAO hallDAO;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private SeatTypeDAO seatTypeDAO;
    @LocalServerPort
    private int port;
    @Autowired
    private JWTCreator jwtCreator;
    @Autowired
    private UserDAO userDAO;

    private Movie movie;
    private Hall hall;

    @BeforeEach
    void setup() {
        User user = userDAO.save(UserFactory.createAdmin());
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().getValue())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d/schedules".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
        SeatType seatType = seatTypeDAO.save(new SeatType(null, "STANDARD"));
        movie = movieDAO.save(MovieFactory.create());
        hall = hallDAO.save(HallFactory.create(seatType));
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
        Schedule preExistingSchedule = scheduleDAO.save(ScheduleFactory.create(hall, movie));
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
