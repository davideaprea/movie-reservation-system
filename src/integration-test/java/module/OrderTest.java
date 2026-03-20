package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.booking.repository.BookingDAO;
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
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.repository.CompletionDAO;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.entity.ScheduleSeat;
import factory.HallFactory;
import factory.MovieFactory;
import factory.ScheduleFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class OrderTest {
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
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private PaymentDAO paymentDAO;
    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private CompletionDAO completionDAO;

    private Schedule schedule;

    @BeforeEach
    void setup() {
        SeatType seatType = seatTypeDAO.save(new SeatType(null, "STANDARD"));
        Movie movie = movieDAO.save(MovieFactory.create());
        Hall hall = hallDAO.save(HallFactory.create(seatType));
        schedule = scheduleDAO.save(ScheduleFactory.create(hall, movie));
    }

    @SneakyThrows
    @Test
    void test() {
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
        assertThat(payment.getPrice()).isEqualTo(selectedSeats.stream()
                .map(ScheduleSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Booking booking = bookingDAO.findById(response.booking().id()).get();

        assertThat(bookingDAO.count()).isEqualTo(request.seatIds().size());
        assertThat(booking.getScheduleId()).isEqualTo(schedule.getId());
        assertThat(booking.getSeatReservations().size()).isEqualTo(selectedSeats.size());
        assertThat(booking.getSeatReservations())
                .extracting(SeatReservation::getId)
                .containsExactlyInAnyOrderElementsOf(request.seatIds());

        OrderCompletionResponse completionResponse = restTestClient.patch().uri("/orders/" + response.id())
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(OrderCompletionResponse.class)
                .returnResult().getResponseBody();

        assert completionResponse != null;

        Completion completion = completionDAO.findById(completionResponse.id()).get();

        assertThat(completionDAO.count()).isEqualTo(1);
        assertThat(completion.getGatewayCompletionId()).isNotBlank();
        assertThat(completion.getPayment().getId()).isEqualTo(payment.getId());
    }
}
