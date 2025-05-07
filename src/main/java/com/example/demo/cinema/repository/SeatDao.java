package com.example.demo.cinema.repository;

import com.example.demo.cinema.entity.Seat;
import org.springframework.data.repository.CrudRepository;

public interface SeatDao extends CrudRepository<Seat, Long> {
}
