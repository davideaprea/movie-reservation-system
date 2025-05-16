package com.example.demo.integration;

import com.example.demo.booking.dto.BookingDto;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.cinema.enumeration.HallStatus;
import com.example.demo.cinema.enumeration.SeatType;
import com.example.demo.cinema.repository.HallDao;
import com.example.demo.cinema.repository.MovieDao;
import com.example.demo.cinema.repository.ScheduleDao;
import com.example.demo.config.DBManager;
import com.example.demo.config.TestcontainersConfig;
import com.example.demo.core.enumeration.Routes;
import com.example.demo.security.component.JWTManager;
import com.example.demo.security.entity.User;
import com.example.demo.security.enumeration.Roles;
import com.example.demo.security.repository.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private String jwt;

    private long scheduleId;

    @Autowired
    private JWTManager jwtManager;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        jwt = signUser();
        scheduleId = createSchedule().getId();
    }

    @Test
    void givenBookingDto_whenBookingSeats_thenStatusOk() throws Exception {
        BookingDto dto = new BookingDto(
                List.of(1L, 2L, 3L, 4L),
                scheduleId
        );

        String json = objectMapper.writeValueAsString(dto);

        mockMvc
                .perform(post(Routes.PAYMENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", jwt))
                .andExpect(status().isCreated());
    }

    private String signUser() {
        User newUser = new User(
                null,
                "email@.gmail.com",
                "psw",
                Roles.USER
        );

        userDao.save(newUser);

        return "Bearer " + jwtManager.generateToken(newUser.getEmail());
    }

    private Hall createHall() {
        return hallDao.save(Hall.create(
                HallStatus.AVAILABLE,
                createSeats()
        ));
    }

    private List<Seat> createSeats() {
        List<Seat> seats = new ArrayList<>();

        for(int r = 1; r < 10; r++) {
            for(int s = 1; s < 30; s++) {
                seats.add(Seat.create(
                        SeatType.REGULAR,
                        r,
                        s
                ));
            }
        }

        return seats;
    }

    private Movie createMovie() {
        return movieDao.save(Movie.create(
                "Title",
                110,
                "Description",
                "cover"
        ));
    }

    private Schedule createSchedule() {
        long hallId = createHall().getId();
        long movieId = createMovie().getId();
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        return scheduleDao.save(Schedule.create(
                movieId,
                hallId,
                startTime,
                endTime
        ));
    }
}
