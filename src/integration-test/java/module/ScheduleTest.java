package module;

import annotation.ContainerizedContextTest;
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
import com.mrs.app.shared.exception.ConflictingResourceError;
import factory.HallFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;
import factory.MovieFactory;
import factory.ScheduleFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class ScheduleTest {
    @Autowired
    private RestTestClient restTestClient;
    @Autowired
    private ScheduleDAO scheduleDAO;
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private HallDAO hallDAO;
    @Autowired
    private SeatTypeDAO seatTypeDAO;

    private Movie movie;
    private Hall hall;

    @BeforeEach
    void setup() {
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
}
