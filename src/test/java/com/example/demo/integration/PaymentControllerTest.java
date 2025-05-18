package com.example.demo.integration;

import com.example.demo.booking.dto.BookingDto;
import com.example.demo.booking.entity.Booking;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.repository.BookingDao;
import com.example.demo.booking.repository.PaymentDao;
import com.example.demo.booking.response.PayPalOrder;
import com.example.demo.booking.service.PayPalService;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.cinema.enumeration.HallStatus;
import com.example.demo.cinema.enumeration.SeatType;
import com.example.demo.cinema.repository.HallDao;
import com.example.demo.cinema.repository.MovieDao;
import com.example.demo.cinema.repository.ScheduleDao;
import com.example.demo.cinema.repository.SeatDao;
import com.example.demo.config.DBManager;
import com.example.demo.config.TestcontainersConfig;
import com.example.demo.core.enumeration.Routes;
import com.example.demo.security.component.JWTManager;
import com.example.demo.security.entity.User;
import com.example.demo.security.enumeration.Roles;
import com.example.demo.security.repository.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestExecutionListeners(value = DBManager.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@AutoConfigureMockMvc
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private HallDao hallDao;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private SeatDao seatDao;

    private long userId;

    private String jwt;

    private Schedule schedule;

    private Movie movie;

    private Hall hall;

    private List<Seat> hallSeats;

    @Autowired
    private JWTManager jwtManager;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PayPalService payPalService;

    private final Faker faker = new Faker();

    @BeforeEach
    void beforeEach() {
        User user = createUser();
        userId = user.getId();
        jwt = signUser(user.getEmail());

        hall = hallDao.save(Hall.create(HallStatus.AVAILABLE));
        hallSeats = createSeats(10, 30, hall.getId());
        movie = createMovie();
        schedule = createSchedule(
                movie.getId(),
                hall.getId()
        );
    }

    @Test
    void givenBookingDto_whenBookingSeats_thenStatusOk() throws Exception {
        Mockito
                .when(payPalService.createOrder(Mockito.any()))
                .thenReturn(new PayPalOrder("PP_ORDER_1"));

        Mockito
                .when(payPalService.captureOrder(Mockito.any()))
                .thenReturn("PP_CAPTURE_1");

        List<Long> seatIds = hallSeats
                .subList(0, 5)
                .stream()
                .map(Seat::getId)
                .toList();

        BookingDto dto = new BookingDto(
                seatIds,
                schedule.getId()
        );

        String res = postPaymentApi(dto)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Payment payment = objectMapper.readValue(res, Payment.class);

        Assertions.assertEquals(BigDecimal.valueOf(25.0), payment.getPrice());

        patchPaymentApi(payment.getOrderId())
                .andExpect(status().isNoContent());

        Payment updatePayment = paymentDao
                .findById(payment.getId())
                .get();

        Assertions.assertNotNull(updatePayment.getCaptureId());
    }

    @Test
    void givenAlreadyBookedSeats_whenBookingSeats_thenStatusConflict() throws Exception {
        Mockito
                .when(payPalService.createOrder(Mockito.any()))
                .thenReturn(new PayPalOrder("PP_ORDER_2"));

        Mockito
                .when(payPalService.captureOrder(Mockito.any()))
                .thenReturn("PP_CAPTURE_2");

        User user = createUser();
        Seat seat = hallSeats.getFirst();

        Payment payment = paymentDao.save(Payment.create(
                "PP_ORDER_1",
                BigDecimal.valueOf(10),
                user.getId()
        ));

        bookingDao.save(Booking.create(
                payment.getId(),
                seat.getId(),
                schedule.getId()
        ));

        BookingDto dto = new BookingDto(List.of(seat.getId()), schedule.getId());

        postPaymentApi(dto).andExpect(status().isConflict());

        Assertions.assertEquals(1, bookingDao.count());
        Assertions.assertEquals(1, paymentDao.count());
    }

    @Test
    void givenNonAdjacentSeats_whenBookingSeats_thenStatusUnprocessableEntity() throws Exception {
        BookingDto dto = new BookingDto(
                List.of(
                        hallSeats.getFirst().getId(),
                        hallSeats.getLast().getId()
                ),
                schedule.getId()
        );

        postPaymentApi(dto).andExpect(status().isUnprocessableEntity());

        Assertions.assertEquals(0, bookingDao.count());
        Assertions.assertEquals(0, paymentDao.count());
    }

    @Test
    void givenAlreadyCapturedOrderId_whenCapturingOrder_thenStatusIsConflict() throws Exception {
        Mockito
                .when(payPalService.createOrder(Mockito.any()))
                .thenReturn(new PayPalOrder("PP_ORDER_2"));

        Mockito
                .when(payPalService.captureOrder(Mockito.any()))
                .thenReturn("PP_CAPTURE_2");

        Payment payment = paymentDao.save(new Payment(
                null,
                "ORDER_ID",
                "CAPTURE_ID",
                BigDecimal.valueOf(20),
                User.create(userId),
                null
        ));

        Booking booking = bookingDao.save(Booking.create(
                payment.getId(),
                hallSeats.getFirst().getId(),
                schedule.getId()
        ));

        patchPaymentApi(payment.getOrderId())
                .andExpect(status().isConflict());
    }

    private String signUser(String email) {
        return "Bearer " + jwtManager.generateToken(email);
    }

    private List<Seat> createSeats(int rowsNumber, int seatsPerRow, long hallId) {
        List<Seat> seats = new ArrayList<>();

        for (int row = 1; row < rowsNumber; row++) {
            for (int seat = 1; seat < seatsPerRow; seat++) {
                seats.add(Seat.create(
                        SeatType.REGULAR,
                        row,
                        seat,
                        hallId
                ));
            }
        }

        return StreamSupport
                .stream(seatDao.saveAll(seats).spliterator(), false)
                .toList();
    }

    private Movie createMovie() {
        return movieDao.save(Movie.create(
                "Title",
                110,
                "Description",
                "cover"
        ));
    }

    private Schedule createSchedule(long movieId, long hallId) {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        return scheduleDao.save(Schedule.create(
                movieId,
                hallId,
                startTime,
                endTime
        ));
    }

    private ResultActions postPaymentApi(BookingDto dto) throws Exception {
        String json = objectMapper.writeValueAsString(dto);

        return mockMvc.perform(post(Routes.PAYMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", jwt));
    }

    private ResultActions patchPaymentApi(String orderId) throws Exception {
        return mockMvc.perform(patch(Routes.PAYMENTS + "/" + orderId)
                .header("Authorization", jwt));
    }

    private User createUser() {
        User newUser = new User(
                null,
                faker.internet().emailAddress(),
                "psw",
                Roles.USER
        );

        return userDao.save(newUser);
    }
}
