package com.example.demo.security.repository;

import com.example.demo.security.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
