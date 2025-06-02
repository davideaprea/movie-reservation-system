package com.mrs.app.util;

import com.mrs.app.security.component.JWTManager;
import com.mrs.app.security.entity.User;
import com.mrs.app.security.enumeration.Roles;
import com.mrs.app.security.repository.UserDao;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuthUtil {
    private final UserDao userDao;
    private final JWTManager jwtManager;
    private final Faker faker = new Faker();

    public String generateAuthHeader(String email) {
        return "Bearer " + jwtManager.generateToken(email);
    }

    public User createFakeUser(Roles role) {
        User newUser = new User(
                null,
                faker.internet().emailAddress(),
                "psw",
                role
        );

        return userDao.save(newUser);
    }
}
