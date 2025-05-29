package com.example.demo.integration;

import com.example.demo.booking.entity.Booking;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.repository.BookingDao;
import com.example.demo.booking.repository.PaymentDao;
import com.example.demo.cinema.dto.ScheduleDto;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.cinema.enumeration.HallStatus;
import com.example.demo.cinema.enumeration.SeatType;
import com.example.demo.cinema.projection.ScheduleSeatDetails;
import com.example.demo.cinema.repository.HallDao;
import com.example.demo.cinema.repository.MovieDao;
import com.example.demo.cinema.repository.ScheduleDao;
import com.example.demo.cinema.repository.SeatDao;
import com.example.demo.cinema.response.DaySchedule;
import com.example.demo.config.DBManager;
import com.example.demo.config.TestcontainersConfig;
import com.example.demo.core.enumeration.Routes;
import com.example.demo.security.component.JWTManager;
import com.example.demo.security.entity.User;
import com.example.demo.security.enumeration.Roles;
import com.example.demo.security.repository.UserDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestExecutionListeners(value = DBManager.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@AutoConfigureMockMvc
public class ScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private HallDao hallDao;

    @Autowired
    private ObjectMapper objMapper;

    @Autowired
    private JWTManager jwtManager;

    private String jwt;

    private long movieId;

    private long hallId;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private SeatDao seatDao;

    private final Faker faker = new Faker();

    @BeforeEach
    void beforeEach() {
        signUser();
        createHall();
        createMovie();
    }

    @Test
    void givenCorrectSchedule_whenCreatingSchedule_thenStatusCreated() throws Exception {
        ScheduleDto dto = new ScheduleDto(
                movieId,
                hallId,
                LocalDateTime.now().plusDays(1)
        );

        postScheduleApi(dto).andExpect(status().isCreated());
    }

    @Test
    void givenAlreadyTakenHallId_whenCreatingSchedule_thenStatusConflict() throws Exception {
        final LocalDateTime starTime = LocalDateTime.now().plusDays(1);

        scheduleDao.save(Schedule.create(
                movieId,
                hallId,
                starTime,
                starTime.plusHours(2)
        ));

        ScheduleDto dto = new ScheduleDto(
                movieId,
                hallId,
                starTime
        );

        postScheduleApi(dto).andExpect(status().isConflict());

        Assertions.assertEquals(1, scheduleDao.count());
    }

    @Test
    void whenGettingScheduledMovies_thenStatusOk() throws Exception {
        createSchedules();

        String json = mockMvc
                .perform(get(Routes.MOVIES + "/" + movieId + Routes.SCHEDULES))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<DaySchedule> upcomingSchedules = objMapper.readValue(json, new TypeReference<>() {
        });

        final LocalDateTime now = LocalDateTime.now();

        Assertions.assertEquals(5, upcomingSchedules.size());

        for (DaySchedule daySchedule : upcomingSchedules) {
            Assertions.assertNotEquals(0, daySchedule.schedules().size());

            boolean areSchedulesNext = daySchedule
                    .schedules()
                    .stream()
                    .allMatch(schedule -> schedule.startTime().isAfter(now));

            Assertions.assertTrue(areSchedulesNext);
        }
    }

    @Test
    void givenScheduleId_whenGettingScheduleSeats_thenStatusOk() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);

        long scheduleId = saveSchedule(startTime).getId();

        List<Seat> seats = createSeats(10, 20, hallId);

        final long seatId = seats.getFirst().getId();

        saveFakeBooking(seatId, scheduleId);

        String res = mockMvc
                .perform(get(Routes.SCHEDULES + "/" + scheduleId + Routes.SEATS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ScheduleSeatDetails> seatDetails = objMapper.readValue(res, new TypeReference<>() { });

        Assertions.assertEquals(200, seatDetails.size());

        for(ScheduleSeatDetails seat : seatDetails) {
            if(seat.id() == seatId) {
                Assertions.assertFalse(seat.isAvailable());
            }
            else Assertions.assertTrue(seat.isAvailable());
        }
    }

    private void signUser() {
        User newUser = createUser();

        jwt = "Bearer " + jwtManager.generateToken(newUser.getEmail());
    }

    private User createUser() {
        User newUser = new User(
                null,
                faker.internet().emailAddress(),
                "psw",
                Roles.ADMIN
        );

        return userDao.save(newUser);
    }

    private void createHall() {
        hallId = hallDao.save(Hall.create()).getId();
    }

    private void createMovie() {
        movieId = movieDao.save(Movie.create(
                "Title",
                110,
                "Description",
                "cover"
        )).getId();
    }

    private void createSchedules() {
        final LocalDateTime now = LocalDateTime.now();

        List<Schedule> schedules = new ArrayList<>();

        for (int i = -5; i < 0; i++) {
            final LocalDateTime date = now.plusDays(i);

            schedules.add(Schedule.create(
                    movieId,
                    hallId,
                    date,
                    date.plusHours(2)
            ));
        }

        for (int i = 1; i <= 5; i++) {
            final LocalDateTime date = now.plusDays(i);

            schedules.add(Schedule.create(
                    movieId,
                    hallId,
                    date,
                    date.plusHours(2)
            ));
        }

        scheduleDao.saveAll(schedules);
    }

    private ResultActions postScheduleApi(ScheduleDto dto) throws Exception {
        String body = objMapper.writeValueAsString(dto);

        return mockMvc
                .perform(post(Routes.SCHEDULES)
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));
    }

    private List<Seat> createSeats(int rowsNumber, int seatsPerRow, long hallId) {
        List<Seat> seats = new ArrayList<>();

        for (int row = 1; row <= rowsNumber; row++) {
            for (int seat = 1; seat <= seatsPerRow; seat++) {
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

    private Schedule saveSchedule(LocalDateTime starTime) {
        return scheduleDao.save(Schedule.create(
                movieId,
                hallId,
                starTime,
                starTime.plusHours(2)
        ));
    }

    private void saveFakeBooking(long seatId, long scheduleId) {
        Payment payment = paymentDao.save(new Payment(
                null,
                "ORDER_ID",
                "CAPTURE_ID",
                BigDecimal.valueOf(20),
                User.create(createUser().getId()),
                null
        ));

        bookingDao.save(Booking.create(
                payment.getId(),
                seatId,
                scheduleId
        ));
    }
}
