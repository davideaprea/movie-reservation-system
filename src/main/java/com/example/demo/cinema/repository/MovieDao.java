package com.example.demo.cinema.repository;

import com.example.demo.cinema.entity.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieDao extends CrudRepository<Movie, Long> {
}
