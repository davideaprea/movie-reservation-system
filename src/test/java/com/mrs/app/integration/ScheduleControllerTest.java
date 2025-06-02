package com.mrs.app.integration;

import com.mrs.app.cinema.dto.request.ScheduleDto;
import com.mrs.app.cinema.entity.Schedule;
import com.mrs.app.cinema.entity.Seat;
import com.mrs.app.cinema.dto.projection.ScheduleDate;
import com.mrs.app.cinema.dto.projection.ScheduleSeatDetails;
import com.mrs.app.cinema.dto.projection.UpcomingSchedule;
import com.mrs.app.cinema.repository.ScheduleDao;
import com.mrs.app.config.DBManager;
import com.mrs.app.config.TestcontainersConfig;
import com.mrs.app.core.enumeration.Routes;
import com.mrs.app.security.entity.User;
import com.mrs.app.security.enumeration.Roles;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrs.app.util.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private ObjectMapper objMapper;

    private String jwt;

    private long movieId;

    private long hallId;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private HallUtil hallUtil;

    @Autowired
    private MovieUtil movieUtil;

    @Autowired
    private ScheduleUtil scheduleUtil;

    @Autowired
    private BookingUtil bookingUtil;

    @BeforeEach
    void beforeEach() {
        signUser();
        hallId = hallUtil.createFakeHall().getId();
        movieId = movieUtil.createFakeMovie().getId();
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
    void whenFindingMovieSchedule_thenStatusOk() throws Exception {
        scheduleUtil.createFakeSchedules(
                5,
                movieId,
                hallId
        );

        String json = mockMvc
                .perform(get(Routes.MOVIES + "/" + movieId + Routes.SCHEDULES_DATES))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ScheduleDate> upcomingScheduleDates = objMapper.readValue(json, new TypeReference<>() {
        });

        final LocalDate now = LocalDate.now();

        Assertions.assertEquals(5, upcomingScheduleDates.size());

        for(int i = 1; i < upcomingScheduleDates.size(); i++) {
            LocalDate curr = upcomingScheduleDates.get(i).date();
            LocalDate prev = upcomingScheduleDates.get(i - 1).date();

            Assertions.assertFalse(curr.isBefore(now));
            Assertions.assertTrue(curr.isAfter(prev));
        }
    }

    @Test
    void givenScheduleId_whenGettingScheduleSeats_thenStatusOk() throws Exception {
        long scheduleId = scheduleUtil.createFakeSchedule(movieId, hallId).getId();

        List<Seat> seats = hallUtil.createSeats(10, 20, hallId);

        final long seatId = seats.getFirst().getId();

        bookingUtil.createFakeBooking(seatId, scheduleId);

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

    @Test
    void givenScheduleDate_whenFindingDailyMovieSchedules_thenStatusOk() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        scheduleUtil.createSchedulesOnDay(movieId, hallId, today, 3);
        scheduleUtil.createSchedulesOnDay(movieId, hallId, tomorrow, 3);

        String res = mockMvc
                .perform(get(Routes.MOVIES + "/" + movieId + Routes.SCHEDULES_DATES + "/" + tomorrow))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<UpcomingSchedule> upcomingSchedules = objMapper.readValue(res, new TypeReference<>() { });

        Assertions.assertEquals(3, upcomingSchedules.size());

        for (UpcomingSchedule schedule : upcomingSchedules) {
            Assertions.assertTrue(schedule.startTime().toLocalDate().equals(tomorrow));
        }
    }

    private void signUser() {
        User newUser = authUtil.createFakeUser(Roles.ADMIN);

        jwt = authUtil.generateAuthHeader(newUser.getEmail());
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
