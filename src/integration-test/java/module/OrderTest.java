package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.booking.repository.BookingRepository;
import com.mrs.app.booking.repository.SeatReservationRepository;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.repository.HallRepository;
import com.mrs.app.hall.repository.SeatTypeRepository;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.MovieRepository;
import com.mrs.app.order.repository.OrderRepository;
import com.mrs.app.order.dto.HTTPOrderCreateRequest;
import com.mrs.app.order.dto.OrderCreateResponse;
import com.mrs.app.order.dto.OrderGetResponse;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;
import com.mrs.app.payment.entity.Intent;
import com.mrs.app.payment.repository.CompletionRepository;
import com.mrs.app.payment.repository.IntentRepository;
import com.mrs.app.schedule.repository.ScheduleRepository;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.entity.ScheduleSeat;
import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.repository.UserRepository;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
import com.mrs.app.shared.exception.ConflictingResourceError;
import factory.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContainerizedContextTest
public class OrderTest {
    private RestTestClient restTestClient;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private SeatTypeRepository seatTypeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IntentRepository intentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CompletionRepository completionRepository;
    @Autowired
    private SeatReservationRepository seatReservationRepository;
    @Autowired
    private PaymentGateway paymentGateway;
    @Autowired
    private UserRepository userRepository;
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
        loggedUser = userRepository.save(UserFactory.createUser());
        String jwt = jwtCreator.withSubject(new JWTClaims(loggedUser.getEmail(), List.of(loggedUser.getRole().getValue())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
        SeatType seatType = seatTypeRepository.save(new SeatType(null, "STANDARD"));
        Movie movie = movieRepository.save(MovieFactory.create());
        Hall hall = hallRepository.save(HallFactory.create(seatType));
        schedule = scheduleRepository.save(ScheduleFactory.create(hall, movie));
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

        Order order = orderRepository.findById(response.id()).get();

        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(order.getUserId()).isEqualTo(loggedUser.getId());

        Intent intent = intentRepository.findById(response.intent().id()).get();

        assertThat(intentRepository.count()).isEqualTo(1);
        assertThat(intent.getAmount()).isEqualByComparingTo(selectedSeats.stream()
                .map(ScheduleSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        assertThat(Duration.between(intent.getCreatedAt(), intent.getExpiresAt())).isEqualTo(paymentTimeout);

        Booking booking = bookingRepository.findById(response.booking().id()).get();

        assertThat(bookingRepository.count()).isEqualTo(1);
        assertThat(seatReservationRepository.count()).isEqualTo(selectedSeats.size());
        assertThat(booking.getScheduleId()).isEqualTo(schedule.getId());

        List<SeatReservation> seatReservations = seatReservationRepository.findAllByBookingId(booking.getId());

        assertThat(seatReservations.size()).isEqualTo(selectedSeats.size());
        assertThat(seatReservations)
                .extracting(SeatReservation::getScheduleSeatId)
                .containsExactlyInAnyOrderElementsOf(request.seatIds());

        assertThat(completionRepository.count()).isEqualTo(0);
    }

    @Test
    void givenAlreadyBookedSeats_whenBookingScheduleSeats_thenStatusConflict() {
        long selectedSeatId = schedule.getSeats().getFirst().getId();
        Booking booking = bookingRepository.save(BookingFactory.create(schedule, List.of(selectedSeatId)));
        HTTPOrderCreateRequest request = new HTTPOrderCreateRequest(
                schedule.getId(),
                List.of(schedule.getSeats().getFirst().getId())
        );

        restTestClient.post().uri("/orders")
                .body(request).exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ConflictingResourceError.class);

        assertThat(bookingRepository.count()).isEqualTo(1);
        assertThat(bookingRepository.existsById(booking.getId()));
        assertThat(seatReservationRepository.count()).isEqualTo(1);
        assertThat(orderRepository.count()).isEqualTo(0);
        assertThat(intentRepository.count()).isEqualTo(0);
    }

    @Test
    void givenAuthenticatedUser_whenRetrievingOrders_thenStatusOk() {
        Booking loggedUserBooking = bookingRepository.save(BookingFactory.create(schedule, List.of(schedule.getSeats().getFirst().getId())));
        Intent loggedUserIntent = intentRepository.save(PaymentFactory.create(paymentTimeout));
        Order loggedUserOrder = orderRepository.save(Order.builder()
                .userId(loggedUser.getId())
                .createdAt(LocalDateTime.now())
                .bookingId(loggedUserBooking.getId())
                .intentId(loggedUserIntent.getId())
                .build());

        User differentUser = userRepository.save(UserFactory.createUser());
        Booking differentUserBooking = bookingRepository.save(BookingFactory.create(schedule, List.of(schedule.getSeats().getLast().getId())));
        Intent differentUserIntent = intentRepository.save(PaymentFactory.create(paymentTimeout));

        orderRepository.save(Order.builder()
                .userId(differentUser.getId())
                .createdAt(LocalDateTime.now())
                .bookingId(differentUserBooking.getId())
                .intentId(differentUserIntent.getId())
                .build());

        List<OrderGetResponse> response = restTestClient.get().uri("/orders").exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<OrderGetResponse>>() {
                })
                .returnResult().getResponseBody();
        OrderGetResponse firstOrder = response.getFirst();

        assertThat(response).hasSize(1);
        assertThat(firstOrder.id()).isEqualTo(loggedUserOrder.getId());
        assertThat(firstOrder.booking().id()).isEqualTo(loggedUserBooking.getId());
        assertThat(firstOrder.intent().id()).isEqualTo(loggedUserIntent.getId());
    }
}
