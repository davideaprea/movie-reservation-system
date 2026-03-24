package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallResponse;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.hall.repository.SeatDAO;
import com.mrs.app.hall.repository.SeatTypeDAO;
import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.dao.UserDAO;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
import factory.HallFactory;
import factory.UserFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class HallTest {
    private RestTestClient restTestClient;
    @Autowired
    private HallDAO hallDAO;
    @Autowired
    private SeatDAO seatDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private JWTCreator jwtCreator;
    @LocalServerPort
    private int port;
    @Autowired
    private SeatTypeDAO seatTypeDAO;
    private SeatType standardSeatType;

    @BeforeEach
    void setup() {
        User user = userDAO.save(UserFactory.createAdmin());
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().getValue())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
        standardSeatType = seatTypeDAO.save(new SeatType(null, "STANDARD"));
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingHall_thenStatusCreated() {
        int rowsNumber = 5;
        int seatsPerRow = 5;
        HallCreateRequest request = HallFactory.createRequest(standardSeatType.getId(), rowsNumber, seatsPerRow);

        restTestClient.post().uri("/halls")
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(HallResponse.class);

        assertThat(hallDAO.count()).isEqualTo(1);
        assertThat(seatDAO.count()).isEqualTo(rowsNumber * seatsPerRow);
    }
}
