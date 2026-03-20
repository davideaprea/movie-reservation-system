package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.booking.repository.BookingDAO;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.hall.repository.SeatTypeDAO;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.MovieDAO;
import com.mrs.app.order.dao.OrderDAO;
import com.mrs.app.order.dto.HTTPOrderCreateRequest;
import com.mrs.app.order.dto.OrderCreateResponse;
import com.mrs.app.order.entity.Order;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.entity.Schedule;
import factory.HallFactory;
import factory.MovieFactory;
import factory.ScheduleFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

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
        HTTPOrderCreateRequest request = new HTTPOrderCreateRequest(
                schedule.getId(),
                List.of(schedule.getSeats().getFirst().getId())
        );
        OrderCreateResponse response = restTestClient.post().uri("/orders")
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(OrderCreateResponse.class)
                .returnResult().getResponseBody();

        assert response != null;

        Order order = orderDAO.findById(response.id()).get();

        assertThat(orderDAO.count()).isEqualTo(1);
        assertThat(paymentDAO.count()).isEqualTo(1);
        assertThat(bookingDAO.count()).isEqualTo(request.seatIds().size());
    }
}
