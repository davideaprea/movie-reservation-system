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
import com.mrs.app.payment.dto.GatewayOrderCompletionResponse;
import com.mrs.app.payment.dto.GatewayOrderCreateResponse;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.repository.CompletionDAO;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.entity.ScheduleSeat;
import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.dao.UserDAO;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
import factory.HallFactory;
import factory.MovieFactory;
import factory.ScheduleFactory;
import factory.UserFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        Mockito.when(paymentGateway.createOrder(Mockito.any())).thenReturn(new GatewayOrderCreateResponse("order-id"));

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

        Payment payment = paymentDAO.findById(response.payment().id()).get();

        assertThat(paymentDAO.count()).isEqualTo(1);
        assertThat(payment.getGatewayOrderId()).isNotBlank();
        assertThat(payment.getPrice()).isEqualByComparingTo(selectedSeats.stream()
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

    @SneakyThrows
    @Test
    void givenValidPayload_whenCompletingBookingPayment_thenStatusOk() {
        Mockito.when(paymentGateway.completeOrder(Mockito.any())).thenReturn(new GatewayOrderCompletionResponse("order-id", "capture-id"));

        Booking bookingToSave = new Booking(null, new ArrayList<>(), schedule.getId());

        bookingToSave.addSeatReservation(new SeatReservation(null, schedule.getSeats().getFirst().getId(), bookingToSave));

        Booking booking = bookingDAO.save(bookingToSave);
        Payment payment = paymentDAO.save(new Payment(null, "order-id", BigDecimal.valueOf(5)));
        Order order = orderDAO.save(new Order(null, payment.getId(), loggedUser.getId(), booking.getId()));
        OrderCompletionResponse completionResponse = restTestClient.patch().uri("/orders/" + order.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderCompletionResponse.class)
                .returnResult().getResponseBody();

        assert completionResponse != null;

        Completion completion = completionDAO.findById(completionResponse.id()).get();

        assertThat(completionDAO.count()).isEqualTo(1);
        assertThat(completion.getGatewayCompletionId()).isNotBlank();
        assertThat(completion.getPayment().getId()).isEqualTo(payment.getId());
    }
}
