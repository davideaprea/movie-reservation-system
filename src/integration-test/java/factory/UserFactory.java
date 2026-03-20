package factory;

import com.mrs.app.security.entity.User;
import com.mrs.app.security.enumeration.Roles;

public class UserFactory {
    private UserFactory() {
    }

    public static User createAdmin() {
        return new User(null, "admin@domain.com", "bcrypt password", Roles.ADMIN);
    }
}
