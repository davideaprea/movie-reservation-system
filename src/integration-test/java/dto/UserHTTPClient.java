package dto;

import com.mrs.app.security.entity.User;
import org.springframework.test.web.servlet.client.RestTestClient;

public record UserHTTPClient(
        User user,
        RestTestClient client
) {
}
