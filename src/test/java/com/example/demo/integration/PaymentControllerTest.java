package com.example.demo.integration;

import com.example.demo.booking.dto.PaymentDto;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.repository.BookingDao;
import com.example.demo.booking.repository.PaymentDao;
import com.example.demo.booking.response.PayPalCapturedOrder;
import com.example.demo.booking.response.PayPalOrder;
import com.example.demo.booking.service.PayPalService;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.config.DBManager;
import com.example.demo.config.TestcontainersConfig;
import com.example.demo.core.enumeration.Routes;
import com.example.demo.security.entity.User;
import com.example.demo.security.enumeration.Roles;
import com.example.demo.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.UUID;

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
    private AuthUtil authUtil;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private PaymentDao paymentDao;

    private long userId;

    private String jwt;

    private Schedule schedule;

    private Movie movie;

    private Hall hall;

    private List<Seat> hallSeats;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PayPalService payPalService;

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
        mockPayPalOrderCreation();
        mockPayPalOrderCapture();

        User user = authUtil.createFakeUser(Roles.USER);
        userId = user.getId();
        jwt = authUtil.generateAuthHeader(user.getEmail());

        hall = hallUtil.createFakeHall();
        hallSeats = hallUtil.createSeats(10, 30, hall.getId());
        movie = movieUtil.createFakeMovie();
        schedule = scheduleUtil.createFakeSchedule(
                movie.getId(),
                hall.getId()
        );
    }

    @Test
    void givenBookingDto_whenBookingSeats_thenStatusOk() throws Exception {
        PaymentDto dto = new PaymentDto(
                getRandomAdjacentSeatIds(),
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
        Seat seat = hallSeats.getFirst();

        bookingUtil.createFakeBooking(seat.getId(), schedule.getId());

        PaymentDto dto = new PaymentDto(List.of(seat.getId()), schedule.getId());

        postPaymentApi(dto).andExpect(status().isConflict());

        Assertions.assertEquals(1, bookingDao.count());
        Assertions.assertEquals(1, paymentDao.count());
    }

    @Test
    void givenNonAdjacentSeats_whenBookingSeats_thenStatusUnprocessableEntity() throws Exception {
        PaymentDto dto = new PaymentDto(
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
    void givenAlreadyCapturedOrderId_whenCapturingOrder_thenStatusConflict() throws Exception {
        Payment payment = bookingUtil.createFakeBooking(
                hallSeats.getFirst().getId(),
                schedule.getId()
        );

        patchPaymentApi(payment.getOrderId())
                .andExpect(status().isConflict());
    }

    private ResultActions postPaymentApi(PaymentDto dto) throws Exception {
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

    private void mockPayPalOrderCreation() {
        Mockito
                .when(payPalService.createOrder(Mockito.any()))
                .thenReturn(new PayPalOrder(UUID.randomUUID().toString()));
    }

    private void mockPayPalOrderCapture() {
        PayPalCapturedOrder.Capture capture = new PayPalCapturedOrder.Capture(UUID.randomUUID().toString());
        PayPalCapturedOrder.Payments payments = new PayPalCapturedOrder.Payments(List.of(capture));
        PayPalCapturedOrder.PurchaseUnit purchaseUnit = new PayPalCapturedOrder.PurchaseUnit(payments);
        PayPalCapturedOrder capturedOrder = new PayPalCapturedOrder(List.of(purchaseUnit));

        Mockito
                .when(payPalService.captureOrder(Mockito.any()))
                .thenReturn(capturedOrder);
    }

    private List<Long> getRandomAdjacentSeatIds() {
        return hallSeats
                .subList(0, 5)
                .stream()
                .map(Seat::getId)
                .toList();
    }
}
