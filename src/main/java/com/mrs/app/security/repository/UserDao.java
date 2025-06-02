package com.mrs.app.security.repository;

import com.mrs.app.security.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
