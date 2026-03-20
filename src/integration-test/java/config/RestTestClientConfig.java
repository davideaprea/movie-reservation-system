package config;

import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.dao.UserDAO;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
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
    @Bean
    public RestTestClient configClient(UserDAO userDAO, JWTCreator jwtCreator, @LocalServerPort int port) {
        User user = userDAO.save(UserFactory.createAdmin());
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().getValue())));

        return RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
    }
}
