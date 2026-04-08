package module;

import annotation.ContainerizedContextTest;
import io.github.davideaprea.hall.entity.Hall;
import io.github.davideaprea.hall.entity.SeatType;
import io.github.davideaprea.hall.repository.HallDAO;
import io.github.davideaprea.hall.repository.SeatTypeDAO;
import io.github.davideaprea.movie.entity.Movie;
import io.github.davideaprea.movie.repository.MovieDAO;
import io.github.davideaprea.schedule.dao.ScheduleDAO;
import io.github.davideaprea.schedule.dto.ScheduleCreateRequest;
import io.github.davideaprea.schedule.dto.ScheduleResponse;
import io.github.davideaprea.schedule.dto.ScheduleSeatResponse;
import io.github.davideaprea.schedule.entity.Schedule;
import io.github.davideaprea.security.component.JWTCreator;
import io.github.davideaprea.security.dao.UserDAO;
import io.github.davideaprea.security.dto.JWTClaims;
import io.github.davideaprea.security.entity.User;
import io.github.davideaprea.shared.exception.ConflictingResourceError;
import factory.HallFactory;
import factory.UserFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;
import factory.MovieFactory;
import factory.ScheduleFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class ScheduleTest {
    private RestTestClient restTestClient;
    @Autowired
    private ScheduleDAO scheduleDAO;
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private HallDAO hallDAO;
    @Autowired
    private SeatTypeDAO seatTypeDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private JWTCreator jwtCreator;
    @LocalServerPort
    private int port;

    private Movie movie;
    private Hall hall;

    @BeforeEach
    void setup() {
        User user = userDAO.save(UserFactory.createAdmin());
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().getValue())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
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
        ScheduleResponse actualResponse = restTestClient.post().uri("/schedules")
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(ScheduleResponse.class)
                .returnResult().getResponseBody();

        assert actualResponse != null;

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
        ConflictingResourceError<ScheduleResponse> response = restTestClient.post().uri("/schedules")
                .body(conflictingRequest).exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(new ParameterizedTypeReference<ConflictingResourceError<ScheduleResponse>>() {
                })
                .returnResult().getResponseBody();

        assert response != null;

        assertThat(scheduleDAO.count()).isEqualTo(1);
        assertThat(scheduleDAO.existsById(preExistingSchedule.getId())).isTrue();
        assertThat(response.conflictingResources().size()).isEqualTo(1);
        assertThat(response.conflictingResources().getFirst().id()).isEqualTo(preExistingSchedule.getId());
        assertThat(response.violatingFields()).isEqualTo(List.of(ScheduleCreateRequest.Fields.startTime, ScheduleCreateRequest.Fields.hallId));
    }

    @SneakyThrows
    @Test
    void givenSearchFilters_whenGettingSchedules_thenStatusOk() {
        Movie differentMovie = movieDAO.save(MovieFactory.create());
        LocalDate today = LocalDate.now();
        LocalDateTime tomorrow = today.atStartOfDay().plusDays(1);
        LocalDateTime yesterday = today.atStartOfDay().minusDays(1);
        LocalDateTime dayAfterTomorrow = tomorrow.plusDays(1);
        Schedule yesterdaySchedule = Schedule.builder()
                .hallId(hall.getId())
                .movieId(movie.getId())
                .startTime(yesterday.withHour(10))
                .endTime(yesterday.withHour(12))
                .build();
        Schedule tomorrowSchedule = Schedule.builder()
                .hallId(hall.getId())
                .movieId(movie.getId())
                .startTime(tomorrow.withHour(10))
                .endTime(tomorrow.withHour(12))
                .build();
        Schedule dayAfterTomorrowSchedule = Schedule.builder()
                .hallId(hall.getId())
                .movieId(movie.getId())
                .startTime(dayAfterTomorrow.withHour(10))
                .endTime(dayAfterTomorrow.withHour(12))
                .build();
        Schedule differentMovieSchedule = Schedule.builder()
                .hallId(hall.getId())
                .movieId(differentMovie.getId())
                .startTime(tomorrow.withHour(14))
                .endTime(tomorrow.withHour(16))
                .build();

        scheduleDAO.saveAll(List.of(yesterdaySchedule, tomorrowSchedule, dayAfterTomorrowSchedule, differentMovieSchedule));

        List<ScheduleResponse> response = restTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/schedules")
                        .queryParam("movieId", movie.getId())
                        .queryParam("startTimeFrom", tomorrow)
                        .queryParam("endTimeTo", tomorrow.withHour(23)).build())
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<ScheduleResponse>>() {
                }).returnResult().getResponseBody();

        assert response != null;

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.stream().allMatch(schedule ->
                schedule.movieId() == movie.getId() &&
                        schedule.startTime().isAfter(tomorrow) &&
                        schedule.endTime().isBefore(tomorrow.withHour(23))
        )).isTrue();
    }
}
