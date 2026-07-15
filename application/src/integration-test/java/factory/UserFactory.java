package factory;

import com.mrs.security.entity.User;
import com.mrs.security.enumeration.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFactory {
    public static User createAdmin(long cinemaId) {
        return new User(null, UUID.randomUUID() + "@domain.com", "bcrypt password", Role.ADMIN, cinemaId);
    }

    public static User createUser() {
        return new User(null, UUID.randomUUID() + "@domain.com", "bcrypt password", Role.USER, null);
    }

    public static User createOperator(long cinemaId) {
        return new User(null, UUID.randomUUID() + "@domain.com", "bcrypt password", Role.OPERATOR, cinemaId);
    }
}
