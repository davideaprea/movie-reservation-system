package com.example.demo.integration;

import com.example.demo.cinema.dto.ScheduleDto;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.enumeration.HallStatus;
import com.example.demo.cinema.repository.HallDao;
import com.example.demo.cinema.repository.MovieDao;
import com.example.demo.cinema.repository.ScheduleDao;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                starTime.plusMinutes(30)
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

        for(DaySchedule daySchedule : upcomingSchedules) {
            Assertions.assertNotEquals(0, daySchedule.schedules().size());

            boolean areSchedulesNext = daySchedule
                    .schedules()
                    .stream()
                    .allMatch(schedule -> schedule.startTime().isAfter(now));

            Assertions.assertTrue(areSchedulesNext);
        }
    }

    private void signUser() {
        User newUser = new User(
                null,
                "email@.gmail.com",
                "psw",
                Roles.ADMIN
        );

        userDao.save(newUser);

        jwt = "Bearer " + jwtManager.generateToken(newUser.getEmail());
    }

    private void createHall() {
        hallId = hallDao.save(Hall.create(
                HallStatus.AVAILABLE
        )).getId();
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
}
