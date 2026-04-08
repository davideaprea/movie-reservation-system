package module;

import annotation.ContainerizedContextTest;
import io.github.davideaprea.booking.entity.Booking;
import io.github.davideaprea.booking.entity.SeatReservation;
import io.github.davideaprea.booking.repository.BookingDAO;
import io.github.davideaprea.booking.repository.SeatReservationDAO;
import io.github.davideaprea.hall.entity.Hall;
import io.github.davideaprea.hall.entity.SeatType;
import io.github.davideaprea.hall.repository.HallDAO;
import io.github.davideaprea.hall.repository.SeatTypeDAO;
import io.github.davideaprea.movie.entity.Movie;
import io.github.davideaprea.movie.repository.MovieDAO;
import io.github.davideaprea.order.dao.OrderDAO;
import io.github.davideaprea.order.dto.HTTPOrderCreateRequest;
import io.github.davideaprea.order.dto.OrderCreateResponse;
import io.github.davideaprea.order.entity.Order;
import io.github.davideaprea.payment.component.PaymentGateway;
import io.github.davideaprea.payment.dto.gateway.GatewayIntentCreateResponse;
import io.github.davideaprea.payment.entity.Intent;
import io.github.davideaprea.payment.repository.CompletionDAO;
import io.github.davideaprea.payment.repository.IntentDAO;
import io.github.davideaprea.schedule.dao.ScheduleDAO;
import io.github.davideaprea.schedule.entity.Schedule;
import io.github.davideaprea.schedule.entity.ScheduleSeat;
import io.github.davideaprea.security.component.JWTCreator;
import io.github.davideaprea.security.dao.UserDAO;
import io.github.davideaprea.security.dto.JWTClaims;
import io.github.davideaprea.security.entity.User;
import io.github.davideaprea.shared.exception.ConflictingResourceError;
import factory.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class OrderTest {
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
    private OrderDAO orderDAO;
    @Autowired
    private IntentDAO intentDAO;
    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private CompletionDAO completionDAO;
    @Autowired
    private SeatReservationDAO seatReservationDAO;
    @Autowired
    private PaymentGateway paymentGateway;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private JWTCreator jwtCreator;
    @LocalServerPort
    private int port;
    @Value("${app.payment.timeout}")
    private Duration paymentTimeout;

    private Schedule schedule;
    private User loggedUser;

    @BeforeEach
    void setup() {
        loggedUser = userDAO.save(UserFactory.createUser());
        String jwt = jwtCreator.withSubject(new JWTClaims(loggedUser.getEmail(), List.of(loggedUser.getRole().getValue())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
        SeatType seatType = seatTypeDAO.save(new SeatType(null, "STANDARD"));
        Movie movie = movieDAO.save(MovieFactory.create());
        Hall hall = hallDAO.save(HallFactory.create(seatType));
        schedule = scheduleDAO.save(ScheduleFactory.create(hall, movie));
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenBookingScheduleSeats_thenStatusCreated() {
        GatewayIntentCreateResponse gatewayIntentCreateResponse = new GatewayIntentCreateResponse(
                "order-id",
                "client-secret",
                "next-step-name"
        );
        Mockito.when(paymentGateway.createIntent(Mockito.any())).thenReturn(gatewayIntentCreateResponse);

        List<ScheduleSeat> selectedSeats = schedule.getSeats().subList(0, 2);
        HTTPOrderCreateRequest request = new HTTPOrderCreateRequest(
                schedule.getId(),
                selectedSeats.stream().map(ScheduleSeat::getId).toList()
        );
        OrderCreateResponse response = restTestClient.post().uri("/orders")
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(OrderCreateResponse.class)
                .returnResult().getResponseBody();

        Order order = orderDAO.findById(response.id()).get();

        assertThat(orderDAO.count()).isEqualTo(1);
        assertThat(order.getUserId()).isEqualTo(loggedUser.getId());

        Intent intent = intentDAO.findById(response.intent().id()).get();

        assertThat(intentDAO.count()).isEqualTo(1);
        assertThat(intent.getAmount()).isEqualByComparingTo(selectedSeats.stream()
                .map(ScheduleSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        assertThat(Duration.between(intent.getCreatedAt(), intent.getExpiresAt())).isEqualTo(paymentTimeout);

        Booking booking = bookingDAO.findById(response.booking().id()).get();

        assertThat(bookingDAO.count()).isEqualTo(1);
        assertThat(seatReservationDAO.count()).isEqualTo(selectedSeats.size());
        assertThat(booking.getScheduleId()).isEqualTo(schedule.getId());
        assertThat(booking.getSeatReservations().size()).isEqualTo(selectedSeats.size());
        assertThat(booking.getSeatReservations())
                .extracting(SeatReservation::getScheduleSeatId)
                .containsExactlyInAnyOrderElementsOf(request.seatIds());

        assertThat(completionDAO.count()).isEqualTo(0);
    }

    @Test
    void givenAlreadyBookedSeats_whenBookingScheduleSeats_thenStatusConflict() {
        Booking booking = bookingDAO.save(BookingFactory.create(schedule));
        HTTPOrderCreateRequest request = new HTTPOrderCreateRequest(
                schedule.getId(),
                List.of(booking.getSeatReservations().getFirst().getId())
        );

        restTestClient.post().uri("/orders")
                .body(request).exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ConflictingResourceError.class);

        assertThat(bookingDAO.count()).isEqualTo(1);
        assertThat(bookingDAO.existsById(booking.getId()));
        assertThat(seatReservationDAO.count()).isEqualTo(booking.getSeatReservations().size());
        assertThat(orderDAO.count()).isEqualTo(0);
        assertThat(intentDAO.count()).isEqualTo(0);
    }
}
