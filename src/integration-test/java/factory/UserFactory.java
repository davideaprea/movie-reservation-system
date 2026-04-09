package factory;

import com.mrs.app.security.entity.User;
import com.mrs.app.security.enumeration.Role;

import java.util.UUID;

public class UserFactory {
    private UserFactory() {
    }

    public static User createAdmin() {
        return new User(null, UUID.randomUUID() + "@domain.com", "bcrypt password", Role.ADMIN);
    }

    public static User createUser() {
        return new User(null, UUID.randomUUID() + "@domain.com", "bcrypt password", Role.USER);
    }
}
