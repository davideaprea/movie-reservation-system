package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.booking.repository.BookingDAO;
import com.mrs.app.booking.repository.SeatReservationDAO;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.hall.repository.SeatTypeDAO;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.MovieDAO;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.HTTPOrderCreateRequest;
import com.mrs.app.order.dto.OrderCompletionResponse;
import com.mrs.app.order.dto.OrderCreateResponse;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.gateway.GatewayOrderCompletionResponse;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Intent;
import com.mrs.app.payment.repository.CompletionDAO;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.entity.ScheduleSeat;
import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.dao.UserDAO;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
import com.mrs.app.shared.exception.ConflictingResourceError;
import factory.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
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
    private PaymentDAO paymentDAO;
    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private CompletionDAO completionDAO;
    @Autowired
    private SeatReservationDAO seatReservationDAO;
    @MockitoBean
    private PaymentGateway paymentGateway;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private JWTCreator jwtCreator;
    @LocalServerPort
    private int port;

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
        GatewayIntentCreateResponse gatewayIntentCreateResponse = new GatewayIntentCreateResponse("order-id");
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

        assert response != null;

        assertThat(orderDAO.count()).isEqualTo(1);

        Intent intent = paymentDAO.findById(response.payment().id()).get();

        assertThat(paymentDAO.count()).isEqualTo(1);
        assertThat(intent.getGatewayIntentId()).isEqualTo(gatewayIntentCreateResponse.id());
        assertThat(intent.getPrice()).isEqualByComparingTo(selectedSeats.stream()
                .map(ScheduleSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Booking booking = bookingDAO.findById(response.booking().id()).get();

        assertThat(bookingDAO.count()).isEqualTo(1);
        assertThat(seatReservationDAO.count()).isEqualTo(selectedSeats.size());
        assertThat(booking.getScheduleId()).isEqualTo(schedule.getId());
        assertThat(booking.getSeatReservations().size()).isEqualTo(selectedSeats.size());
        assertThat(booking.getSeatReservations())
                .extracting(SeatReservation::getScheduleSeatId)
                .containsExactlyInAnyOrderElementsOf(request.seatIds());
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
        assertThat(paymentDAO.count()).isEqualTo(0);
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenCompletingBookingPayment_thenStatusOk() {
        GatewayOrderCompletionResponse gatewayOrderCompletionResponse = new GatewayOrderCompletionResponse("order-id", "capture-id");
        Mockito.when(paymentGateway.completePayment(Mockito.any())).thenReturn(gatewayOrderCompletionResponse);

        Booking booking = bookingDAO.save(BookingFactory.create(schedule));
        Intent intent = paymentDAO.save(PaymentFactory.create());
        Order order = orderDAO.save(new Order(null, intent.getId(), loggedUser.getId(), booking.getId()));
        OrderCompletionResponse completionResponse = restTestClient.patch().uri("/orders/" + order.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderCompletionResponse.class)
                .returnResult().getResponseBody();

        assert completionResponse != null;

        Completion completion = completionDAO.findById(completionResponse.id()).get();

        assertThat(completionDAO.count()).isEqualTo(1);
        assertThat(completion.getGatewayCompletionId()).isEqualTo(gatewayOrderCompletionResponse.completionId());
        assertThat(completion.getIntent().getId()).isEqualTo(intent.getId());
    }

    @SneakyThrows
    @Test
    void givenAlreadyCompletedPaymentId_whenCompletingPayment_thenStatusConflict() {
        Booking booking = bookingDAO.save(BookingFactory.create(schedule));
        Intent intent = paymentDAO.save(PaymentFactory.create());
        Order order = orderDAO.save(new Order(null, intent.getId(), loggedUser.getId(), booking.getId()));

        completionDAO.save(new Completion(null, intent, "completion-id"));
        restTestClient.patch().uri("/orders/" + order.getId())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ConflictingResourceError.class);

        assertThat(completionDAO.count()).isEqualTo(1);
        Mockito.verify(paymentGateway, Mockito.never()).completePayment(Mockito.any());
    }
}
