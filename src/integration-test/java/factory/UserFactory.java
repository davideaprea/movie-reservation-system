package factory;

import com.github.javafaker.Faker;
import com.mrs.app.security.entity.User;
import com.mrs.app.security.enumeration.Role;

public class UserFactory {
    private static final Faker faker = new Faker();

    private UserFactory() {
    }

    public static User create(Role role) {
        return User.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .role(role)
                .build();
    }
}
