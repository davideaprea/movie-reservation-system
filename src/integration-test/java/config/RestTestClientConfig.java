package config;

import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.dao.UserDAO;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
import com.mrs.app.security.enumeration.Role;
import dto.UserHTTPClient;
import factory.UserFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

@Lazy
@TestConfiguration(proxyBeanMethods = false)
public class RestTestClientConfig {
    private final UserDAO userDAO;
    private final JWTCreator jwtCreator;
    private final int port;

    public RestTestClientConfig(UserDAO userDAO, JWTCreator jwtCreator, @LocalServerPort int port) {
        this.userDAO = userDAO;
        this.jwtCreator = jwtCreator;
        this.port = port;
    }

    @Bean("adminClient")
    public UserHTTPClient configAdminClient() {
        return configRoleClient(Role.ADMIN);
    }

    @Bean("userClient")
    public UserHTTPClient configUserClient() {
        return configRoleClient(Role.USER);
    }

    private UserHTTPClient configRoleClient(Role role) {
        User user = userDAO.save(UserFactory.create(Role.ADMIN));
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().getValue())));
        RestTestClient client = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();

        return new UserHTTPClient(user, client);
    }
}
