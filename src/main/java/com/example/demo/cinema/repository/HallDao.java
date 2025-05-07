package com.example.demo.cinema.repository;

import com.example.demo.cinema.entity.Hall;
import org.springframework.data.repository.CrudRepository;

public interface HallDao extends CrudRepository<Hall, Long> {
}
