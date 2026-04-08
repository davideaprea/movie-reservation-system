package com.mrs.app.booking.controller;

import com.mrs.app.booking.dto.SeatReservationResponse;
import com.mrs.app.booking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<SeatReservationResponse>> findSeatReservationsByScheduleId(
            @RequestParam long scheduleId
    ) {
        return ResponseEntity.ok(bookingService.findSeatReservationsByScheduleId(scheduleId));
    }
}
