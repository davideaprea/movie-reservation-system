package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallResponse;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.repository.HallDAO;
import com.mrs.app.hall.repository.SeatDAO;
import com.mrs.app.hall.repository.SeatTypeDAO;
import dto.UserHTTPClient;
import factory.HallFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class HallTest {
    @Autowired
    @Qualifier("adminClient")
    private UserHTTPClient userClient;
    @Autowired
    private HallDAO hallDAO;
    @Autowired
    private SeatDAO seatDAO;
    @Autowired
    private SeatTypeDAO seatTypeDAO;
    private SeatType standardSeatType;

    @BeforeEach
    void setup() {
        standardSeatType = seatTypeDAO.save(new SeatType(null, "STANDARD"));
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingHall_thenStatusCreated() {
        int rowsNumber = 5;
        int seatsPerRow = 5;
        HallCreateRequest request = HallFactory.createRequest(standardSeatType.getId(), rowsNumber, seatsPerRow);

        userClient.client().post().uri("/halls")
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(HallResponse.class);

        assertThat(hallDAO.count()).isEqualTo(1);
        assertThat(seatDAO.count()).isEqualTo(rowsNumber * seatsPerRow);
    }
}
