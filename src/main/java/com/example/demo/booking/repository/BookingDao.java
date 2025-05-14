package com.example.demo.booking.repository;

import com.example.demo.booking.entity.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingDao extends CrudRepository<Booking, Long> {
}
